package com.drake.project.user.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;

import javax.servlet.http.HttpServletResponse;

import com.drake.common.entity.Category;
import com.drake.project.AbstractExporter;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


public class CategoryPdfExporter extends AbstractExporter{
	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf", "categories_");
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
	
		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);
		
		Paragraph paragraph = new Paragraph("List of Categories", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER); // căn giữa khổ A4
		
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(4); // bên trong là số column
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.2f, 3.5f, 3.0f, 1.7f}); // khoảng giũa
		
		writeTableHeader(table);
		writeTableData(table, listCategories);
		
		document.add(table);
		document.close();
	}
	
	public void writeTableData(PdfPTable table, List<Category> listCategories) {
		for (Category Category : listCategories) {
			table.addCell(String.valueOf(Category.getId()));
			table.addCell(Category.getName());
			table.addCell(Category.getAlias());
			table.addCell(String.valueOf(Category.isEnabled()));
		}
	}
	
	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("ID", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Name", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Alias", font));
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);
	}
}
