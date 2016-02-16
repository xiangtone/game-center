package com.mas.rave.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfWriter;
import com.mas.rave.main.vo.Cp;

/**
 * 生成pdf文件
 * 
 * @author liwei.sz
 * 
 */
public class PdfUtil {

	public static String createPdf(String company, String product_name, Cp cp,int fileId,String name) throws DocumentException, MalformedURLException, IOException {
		DateFormat df1 = new SimpleDateFormat("MMMM dd'th', yyyy", Locale.ENGLISH);
		String date = df1.format(new Date());
		String authorized_territory = cp.getName();

		// 实例化文档对象
		Document document = new Document(PageSize.A4, 100, 0, 30, 5);
		File file = new File(Constant.LOCAL_FILE_PATH + File.separator + cp.getId() +File.separator+fileId+ File.separator + "pdf" + File.separator + cp.getId()+"_"+fileId + ".pdf");
		CrawlerFileUtils.createFile(file.getPath());
		PdfWriter.getInstance(document, new FileOutputStream(file.getPath()));
		document.open();
		
		//设置字字体
		FontSelector selector = new FontSelector();
		selector.addFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, 20));
		selector.addFont(FontFactory.getFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED));
		// 创建段落对象
		Anchor anchorTarget = new Anchor(selector.process("Software Licensing Letter"));
		Paragraph paragraph1 = new Paragraph();
		paragraph1.setIndentationLeft(80);
		paragraph1.add(anchorTarget);
		document.add(paragraph1);
		document.add(Chunk.NEWLINE);

		// 调协第二个段落
		Paragraph paragraph2 = new Paragraph(selector.process("To Whom It May Concern"));
		paragraph2.setIndentationLeft(80);
		document.add(paragraph2);
		document.add(Chunk.NEWLINE);

		// 第三个段落
		String first_content = authorized_territory + " (with its registered office at "+name+" ) " + "is herewith authorizing zApp a non-exclusive and royalty-free license"
				+ " to pre-upload and use the Product onto the Distribution Platform in the Authorized Territory.";

		document.add(selector.process(first_content));
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(selector.process("Product Name: " + product_name)));
		document.add(new Paragraph(selector.process("Distribution Platform: zApp Developer Console")));
		document.add(new Paragraph(selector.process("Authorized Territory: " +company)));
		document.add(Chunk.NEWLINE);

		String third_content = authorized_territory
				+ " warrants that the Product implemented on the basis of this Letter will be exempt from defects and errors and that will be totally compliant with the agreed technical requirements.";

		String four_content = authorized_territory
				+ " represents and warrants that the Product and any part thereof shall not infringe or tort any third Party’s rights including without limitation intellectual property right. ";

		String fif_content = authorized_territory
				+ " agrees to indemnify and hold harmless zApp from any kind of losses, costs, expenses or liabilities, including reasonable attorneys’ fees and costs of settlement, arising out of or resulting from any claim by a third party that the Product supplied by "
				+ authorized_territory
				+ "  or performance under this Software Licensing Letter infringes, violates or otherwise improperly affects any of said third party’s patent, copyright, trademark, trade secret or other intellectual property right(s). In the event that zApp receives notification that, or if in "
				+ authorized_territory
				+ " ’s opinion the Products infringe or may infringe any intellectual property rights of a third party,"
				+ authorized_territory
				+ " shall at its sole discretion and expenses either (i) promptly replace or modify the Product or portions thereof to make it non-infringing, (ii) promptly obtain a right to use such third party intellectual property rights or (iii) in the event either of the foregoing remedies can’t be achieved on a commercially reasonable basis despite the best efforts of "
				+ authorized_territory + ", terminate this Letter and reimburse zApp for any and all damages or losses thus incurred.";

		document.add(new Paragraph(selector.process(third_content)));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(selector.process(four_content)));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(selector.process(fif_content)));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(selector.process("Date: " + date)));
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph(selector.process("Authorized Signatory")));
		document.add(new Paragraph(selector.process(authorized_territory)));
		document.leftMargin();
		document.close();
		return file.getPath();
	}

}
