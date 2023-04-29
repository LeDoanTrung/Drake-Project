package com.drake.project.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.drake.common.entity.product.Product;
import com.drake.project.AbstractExporter;



public class ProductCsvExporter extends AbstractExporter{

public void export(List<Product> listProducts, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader(response, "text/csv", ".csv", "products_");
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), 
				CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"Product ID", "Name"};
		String[] fieldMapping = {"id", "name"};
		csvWriter.writeHeader(csvHeader);
		
		for (Product product : listProducts) {
			csvWriter.write(product, fieldMapping);
		}
		
		csvWriter.close();
	}
}
