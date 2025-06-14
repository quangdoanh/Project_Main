package GUI.Components;

import BUS.KhachHangBUS;
import BUS.NhanVienBUS;
import DAO.KhachHangDAO;
import DAO.NhanVienDAO;
import DTO.OBJECTS.KhachHangDTO;
import DTO.OBJECTS.NhanVienDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JTableExcel {

    public static void exportJTableExcel(JTable table){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn lưu file excel");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx"); // tạo ra để lọc
        fileChooser.setFileFilter(filter); // chỉ hiện những file đã lọc
        //fileChooser.setAcceptAllFileFilterUsed(true);

        int userChoice = fileChooser.showSaveDialog(null); // lưu sự lựa chọn của người dùng 0 or 1,-1

        if(userChoice == JFileChooser.APPROVE_OPTION){
            String filePath = fileChooser.getSelectedFile().getAbsolutePath(); // Lấy đường dẫn đầy đủ

            System.out.println("Đường dẫn file:"+filePath);
//


            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            TableModel model = table.getModel();// Lấy model hiện tại
            Workbook workbook = new XSSFWorkbook(); // Tạo một file Excel mới dạng .xlsx trong bộ nhớ RAM
            Sheet sheet = workbook.createSheet("Sheet1"); // Tạo một sheet mới trong file Excel, với tên là "Sheet1".


            // Tạo header
            Row headerRow = sheet.createRow(0);
            for(int i =0 ; i< model.getColumnCount();i++){
                Cell headerCell = headerRow.createCell(i);
                headerCell.setCellValue(model.getColumnName(i));
                System.out.println("Table"+model.getColumnName(i));
            }
            System.out.println("rowcount:"+model.getRowCount());
            System.out.println("getColumnCount"+model.getColumnCount());
            // Tao cac dong
            for(int i = 0 ; i<model.getRowCount() ; i++){
                    Row dataRow = sheet.createRow(i+1);
                for(int j = 0 ; j<model.getColumnCount() ; j++){
                    Cell dataCell = dataRow.createCell(j);
                    Object value = model.getValueAt(i,j); // giá trị của TableModel model
                    if(value != null) {
                        dataCell.setCellValue(value.toString());
                    }
                }
            }
            for (int i = 0; i < model.getColumnCount(); i++) {
                sheet.autoSizeColumn(i); // thay đổi kịc thước theo nội dung
            }

            // Write the output to a file //  Excel thực sự được tạo và lưu lại trên máy tính của bạn,
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                workbook.close();// giải phóng bộ nhớ
            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        }
    }

    public static  void importJTableExcelKH(KhachHangBUS khBUS){
        File importExcel ;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelJTableImport = null;
        ArrayList<KhachHangDTO> listExcel = new ArrayList<KhachHangDTO>();
        JFileChooser fileChooser = new JFileChooser();
        Workbook workbook = null;
        int userChoice = fileChooser.showSaveDialog(null);
        int k = 0;

        if(userChoice == JFileChooser.APPROVE_OPTION){
            try {
                importExcel = fileChooser.getSelectedFile();
                excelFIS = new FileInputStream(importExcel);
                excelBIS = new BufferedInputStream(excelFIS);

                excelJTableImport = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0); // lấy sheet 1 lưu vào biến

                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    int check = 1;
                    XSSFRow excelRow = excelSheet.getRow(row);
                    int id = KhachHangDAO.getInstance().getAutoIncrement();
                    String tenkh = excelRow.getCell(1).getStringCellValue();
                    String diachi = excelRow.getCell(2).getStringCellValue();
                    String sdt = excelRow.getCell(3).getStringCellValue(); ;
                    if (tenkh.isEmpty()  || !sdt.matches("0\\d{9}") || sdt.length() != 10 || diachi.isEmpty()) {
                        check = 0;
                    }
                    if (check == 1) {
                        System.out.println("Chạy vào đây");
                        khBUS.add(new KhachHangDTO(id, tenkh, sdt, diachi)); // thêm vào db and add vào bảng
                    } else {
                        k += 1;
                    }
                }
                JOptionPane.showMessageDialog(null, "Nhập file thành công");
            }  catch (IOException e) {
                System.out.println("Lỗi đọc file");
                throw new RuntimeException(e);
            }

            if (k != 0) {
                JOptionPane.showMessageDialog(null, "Những dữ liệu không hợp lệ không được thêm vào");
            }
        }

    }

    public static  void importJTableExcelNV(NhanVienBUS nvBUS){
        File importExcel ;
        FileInputStream excelFIS = null;
        BufferedInputStream excelBIS = null;
        XSSFWorkbook excelJTableImport = null;
        ArrayList<KhachHangDTO> listExcel = new ArrayList<KhachHangDTO>();
        JFileChooser fileChooser = new JFileChooser();
        Workbook workbook = null;
        int userChoice = fileChooser.showSaveDialog(null);
        int k = 0;

        if(userChoice == JFileChooser.APPROVE_OPTION){
            try {
                importExcel = fileChooser.getSelectedFile();
                excelFIS = new FileInputStream(importExcel);
                excelBIS = new BufferedInputStream(excelFIS);

                excelJTableImport = new XSSFWorkbook(excelBIS);
                XSSFSheet excelSheet = excelJTableImport.getSheetAt(0); // lấy sheet 1 lưu vào biến



                for (int row = 1; row <= excelSheet.getLastRowNum(); row++) {
                    int check = 1;
                    XSSFRow excelRow = excelSheet.getRow(row);
                    int id = NhanVienDAO.getInstance().getAutoIncrement();
                    String tennv = excelRow.getCell(1).getStringCellValue();
                    String gioitinhStr = excelRow.getCell(2).getStringCellValue();
                    String ngaysinh  = excelRow.getCell(3).getStringCellValue();
                    String sdt = excelRow.getCell(4).getStringCellValue();
                    String email = excelRow.getCell(5).getStringCellValue();

                    int gioitinh = gioitinhStr.equalsIgnoreCase("Nam") ? 1 : 0;



                    if (tennv.isEmpty()
                            || !sdt.matches("0\\d{9}") || sdt.length() != 10
                            || gioitinhStr.isEmpty()
                            || ngaysinh.isEmpty() || !ngaysinh.matches("\\d{4}-\\d{2}-\\d{2}")
                            ||  email.isEmpty() || !email.matches("^[\\w.-]+@[\\w.-]+\\.com$")){
                        check = 0;
                    }
                    if (check == 1) {
                        String input = ngaysinh;
                        String year = input.substring(0, 4); //sẽ cắt chuỗi input từ vị trí 0 đến vị trí 3
                        String month = input.substring(5, 7);
                        String day = input.substring(8, 10);
                        String Ngaysinhoutput = day + month + year;
                        System.out.println("Chạy vào đây");
                        nvBUS.add(new NhanVienDTO(id, tennv, gioitinh, Ngaysinhoutput, sdt, 1,email )); // thêm vào db and add vào bảng
                    } else {
                        k += 1;
                    }
                }
                JOptionPane.showMessageDialog(null, "Nhập file thành công");
            }  catch (IOException e) {
                System.out.println("Lỗi đọc file");
                throw new RuntimeException(e);
            }

            if (k != 0) {
                JOptionPane.showMessageDialog(null, "Những dữ liệu không hợp lệ không được thêm vào");
            }
        }

    }
}
