package com.drake.project.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.drake.common.entity.product.Product;
import com.drake.common.entity.product.ProductImage;
import com.drake.project.FileUploadUtil;



public class ProductSaveHelper {

private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);
	
	static void setProductDetails(String [] detailIDs, String[] detailNames, String[] detailValues, Product product) {
		if(detailNames == null || detailNames.length == 0) return ;
		
		for (int i = 0; i < detailNames.length; i++) {
			String name = detailNames[i];
			String value = detailValues[i];
			Integer id = Integer.parseInt(detailIDs[i]);
			
			if (id != 0) { // những cặp name- value mới đc tạo trong chưa đc save xuống DB
				product.addDetail(id, name, value);
			}
			else if (!name.isEmpty() && !value.isEmpty()) {
				product.addDetail(name, value);
			}
				
			
		}
	}
	
	static void deleteExtraImagesWereRemovedOnForm(Product product) {
		String extraImageDir = "../product-images/"+ product.getId()+ "/extras/";
		Path dirPath = Paths.get(extraImageDir);
		
		try {
			Files.list(dirPath).forEach(file ->{
				String fileName = file.toFile().getName();
				
				if (!product.containsImageName(fileName)) {
					try {
						Files.delete(file);
						LOGGER.error("Delete extra image: "+ fileName);
					} catch (IOException e) {
						LOGGER.error("Could not delete extra image: " + fileName);
					}
				}
			});
		} catch (Exception e) {
			LOGGER.error("Could not list directory: " + dirPath);
		}
	}
	
	static void setExistingExtraImageNames(String[] imageIDs, String [] imageNames, Product product) {
		if (imageIDs == null || imageIDs.length ==0) {
			return;
		}
		
		Set<ProductImage> images = new HashSet<>();
		
		for (int i = 0; i < imageIDs.length; i++) {
			Integer id = Integer.parseInt(imageIDs[i]);
			String name = imageNames[i];
			
			images.add(new ProductImage(id,name,product));
		}
		
		product.setImages(images);
	}
	
	static void saveUploadedImages(MultipartFile mainImageMultipartFile,
			MultipartFile[] extraImageMultipartFiles, Product savedProduct) throws IOException {
		if (!mainImageMultipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipartFile.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipartFile);
		}
			
			
		if (extraImageMultipartFiles.length > 0) {
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras/";
			
			for (MultipartFile multipartFile : extraImageMultipartFiles) {
				if (multipartFile.isEmpty()) continue;
				
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);	
			}
		}
	}
	
	static void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		if (extraImageMultiparts.length > 0) {
			for (MultipartFile multipartFile : extraImageMultiparts) {
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
					
					if (!product.containsImageName(fileName)) {
						product.addExtraImage(fileName);
					}
				}
			}
		}
	}
	
	static void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if (!mainImageMultipart.isEmpty()) {
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}
}
