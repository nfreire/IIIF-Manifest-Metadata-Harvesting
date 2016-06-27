/* ScriptGenerateExcelReports.java - created on 20/05/2016, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.iiif_harvest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Hold the data gathered during a harvest of IIIF sources. 
 * The gathered data can be exported to excel format.
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 */
public class IiifMetadataSampling {
    Map<String, CellStyle> styles;
    Workbook wb;
    int rowIdx=0;
    Sheet sheetOvr;
    
    List<String> imageUrls=new ArrayList<>();

    /**
     * Creates a new instance of this class.
     */
    public IiifMetadataSampling() {
        wb=new HSSFWorkbook();
        createStyles(wb);
        sheetOvr = wb.createSheet("Sample");
        int cellIdx=0;
        Row rowHead = sheetOvr.createRow(rowIdx);
        createCell(rowHead, cellIdx++, "IIIF Top Level Colletion URI", "header");
        createCell(rowHead, cellIdx++, "IIIF Manifest URI", "header");
        createCell(rowHead, cellIdx++, "dcterms:rights", "header");
        createCell(rowHead, cellIdx++, "rdf:seeAlso URI", "header");
        
        sheetOvr.autoSizeColumn(0);
        sheetOvr.autoSizeColumn(1);
        sheetOvr.autoSizeColumn(2);
        sheetOvr.autoSizeColumn(3);
    }

    public void addImage(String url) {
    	imageUrls.add(url);
    }

    private static final Pattern iiifImgSizePattern=Pattern.compile("(.*/)[^/]+(/[^/]+/[^/]+)");
    public void exportImages(File outputFile) {
    	System.out.println("Exporting images: "+imageUrls.size());
    	StringBuilder sb=new StringBuilder();
    	sb.append("<html><body><br /><center><h1><font size='16'>IIIFeana</font></h1></center><br /><br />\n");
    	int imgCnt=0;
    	for (String url: imageUrls) {
    		imgCnt++;
    		if(imgCnt>1000)
    			continue;
    		Matcher m = iiifImgSizePattern.matcher(url);
    		if(m.matches()) {
    			String thumb = m.replaceFirst("$1200,$2");
//    			String thumb = m.replaceFirst("$1:50$2");
    			sb.append("<a href='"+url+"'><img src='"+thumb+"' /></a>\n");
    		}
    	}
    	sb.append("</body>");
    	sb.append("</html>");
    	
    	try {
	    	FileOutputStream out=new FileOutputStream(outputFile);
	    	IOUtils.write(sb.toString(), out);
	    	out.flush();
	    	out.close();
    	} catch (IOException e) {
    		throw new RuntimeException(e.getMessage(), e);
    	}
    }
    
    public void exportImagesMosaic(File outputFile) {
    	System.out.println("Exporting images: "+imageUrls.size());
    	StringBuilder sb=new StringBuilder();
    	sb.append("<html><body><br /><center><h1><font size='16'>IIIFeana Picture Mosaic</font></h1></center><br /><br />\n");
    	int imgCnt=0;
    	for (String url: imageUrls) {
    		imgCnt++;
    		if(imgCnt>1000)
    			continue;
    		Matcher m = iiifImgSizePattern.matcher(url);
    		if(m.matches()) {
    			String thumb = m.replaceFirst("$150,$2");
//    			String thumb = m.replaceFirst("$1!20,20$2");
//    			sb.append("<a href='"+url+"'><img  style='display: block; float: left;' src='"+thumb+"' /></a>\n");
    			if(imgCnt % 20 == 0)
    				sb.append("<a href='"+url+"'><img width='20' height='20' style='display: block;' src='"+thumb+"' /></a>\n");
    			else
    				sb.append("<a href='"+url+"'><img width='20' height='20' style='display: block; float: left;' src='"+thumb+"' /></a>\n");
    		}
    		
    	}
    	sb.append("</body>");
    	sb.append("</html>");
    	
    	try {
    		FileOutputStream out=new FileOutputStream(outputFile);
    		IOUtils.write(sb.toString(), out);
    		out.flush();
    		out.close();
    	} catch (IOException e) {
    		throw new RuntimeException(e.getMessage(), e);
    	}
    }
    
    public void export(File outputFile) {
	    try {
	    	if (!outputFile.getParentFile().exists())
	    		outputFile.getParentFile().mkdirs();
	        FileOutputStream out=new FileOutputStream(outputFile);
	        wb.write(out);
	        out.flush();
	    	out.close();
	    } catch (IOException e) {
	        throw new RuntimeException(e.getMessage(), e);
	    }
    }
    
    public void add(String iiifEndpointCollectionUriOrName, String manifestUri, List<String> seeAlsos, String rights) {
        rowIdx++;
        int cellIdx=0;
        Row rowColl = sheetOvr.createRow(rowIdx);
        createLinkCell(rowColl, cellIdx++, iiifEndpointCollectionUriOrName, "cell");
        createLinkCell(rowColl, cellIdx++, manifestUri, "cell");
        createCell(rowColl, cellIdx++, rights == null ? "" : rights, "cell");
        for(String seeAlso: seeAlsos) {
        	createLinkCell(rowColl, cellIdx++, seeAlso, "cell");
        	break;
        }
        
    }
    
    
    /**
     * @param rowHead
     * @param i
     * @param string
     * @param string2
     */
    private Cell createCell(Row row, int idx, String value, String style) {
        Cell cell = row.createCell(idx);
        cell.setCellValue(value);
        cell.setCellStyle(styles.get(style));
        return cell;
    }
    private Cell createLinkCell(Row row, int idx, String url, String style) {
    	   Hyperlink link = wb.getCreationHelper().createHyperlink(Hyperlink.LINK_URL);
           link.setAddress(url);
           link.setLabel(url);
           Cell cell = row.createCell(idx);
    	cell.setHyperlink(link);
    	cell.setCellValue(url);
    	cell.setCellStyle(styles.get(style));
    	return cell;
    }


    private Cell createCell(Row row, int idx, Number value, String style) {
        Cell cell = row.createCell(idx);
        cell.setCellValue(value.toString());
        cell.setCellStyle(styles.get(style));
        return cell;
    }

    /**
     * Create a library of cell styles
     */
    private Map<String, CellStyle> createStyles(Workbook wb){
        styles = new HashMap<String, CellStyle>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)18);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        titleFont.setColor(IndexedColors.BLUE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);
        
        styles.put("cell", style);

        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short)11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula", style);

        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula_2", style);

        return styles;
    }
}
