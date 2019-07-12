package com.dong.test;

import java.io.File;

import org.apache.fontbox.encoding.Encoding;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDCIDFontType0;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

public class PDFTest {

	public static void main(String[] args) throws Exception {

		File file = new File("C:/Users/XIEDONGXIAO/Desktop/ProformaInvoice.pdf");
		PDFTest.watermarkPDF(file);

	}

	/**
	 * PDFBox
	 * 
	 * @param fileStored
	 * @throws Exception
	 */
	private static void watermarkPDF(File fileStored) throws Exception {
		File tmpPDF;
		PDDocument doc;

		tmpPDF = new File(fileStored.getParent() + System.getProperty("file.separator") + "Tmp_" + fileStored.getName());
		doc = PDDocument.load(fileStored);
//		doc = PDDocument.load(in); // 这里换成输入流也可以
		doc.setAllSecurityToBeRemoved(true);
		for (PDPage page : doc.getPages()) {
			PDPageContentStream cs = new PDPageContentStream(doc, page, PDPageContentStream.AppendMode.APPEND, true, true);
			
//			PDResources resources = page.getResources();
			PDRectangle artBox = page.getCropBox();
			float width = artBox.getWidth();
			float height = artBox.getHeight();
			int xd = 100;
			int yd = 80;
			int col = Float.valueOf(width / xd).intValue();
			int row = Float.valueOf(height / yd).intValue();
			
			String ts = new String("珠海代理商");
			PDFont font = PDType0Font.load(doc, new File("c:/windows/fonts/simyou.ttf")); // 使用本地的语言
//			PDFont font = PDType1Font.HELVETICA_OBLIQUE;
			float fontSize = 16.0f;
			PDExtendedGraphicsState r0 = new PDExtendedGraphicsState();
			// 透明度
			r0.setNonStrokingAlphaConstant(0.2f);
			r0.setAlphaSourceFlag(true);
			cs.setGraphicsStateParameters(r0);
//			cs.setNonStrokingColor(220,220,220);// Gray
			cs.setNonStrokingColor(169,169,169);// Gray
			cs.beginText();
			cs.setFont(font, fontSize);
			// 获取旋转实例
//			cs.setTextMatrix(Matrix.getRotateInstance(20, 350f, 490f));
			// 弧度制: 1 rad = 180 / pi
//			cs.setTextMatrix(Matrix.getRotateInstance(0.5, 50f, 900));
//			cs.showText(ts);
//			cs.setTextMatrix(Matrix.getRotateInstance(0.5, 100f, 0));
//			cs.showText(ts);
			for (int j = 1; j <= row; j++) {
				for (int i = 1; i <= col; i++) {
					if (i == 1) {
						cs.setTextMatrix(Matrix.getRotateInstance(0.5, 30, (row - j + 1) * yd));
						cs.showText(ts);
					} else {
						cs.setTextMatrix(Matrix.getRotateInstance(0.5, 30 + xd * (i - 1), (row - j + 1) * yd));
						cs.showText(ts);
					}
				}
			}
			cs.endText();
			cs.close();
		}
		doc.save(tmpPDF);
//		doc.save(out); // 这里换成输出流也可以
	}

}
