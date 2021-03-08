package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bean.FileBoardVO;
import com.example.demo.bean.FileVO;
import com.example.demo.service.FileBoardService;

@Controller
@RequestMapping("/fileBoard")		//"src/main/resource/templates"에 /fileBoard 경로로 요청 받겠다는 의미 
public class FileBoardController {

	@Autowired
	FileBoardService fileBoardService;
	
	//Create
	@RequestMapping("/insert")
	private String fileBoardInsertForm(@ModelAttribute FileBoardVO board) {
		return "fileBoard/insert";
	}
	@RequestMapping("/insertProc")
	private String fileBoardInsertProc(@ModelAttribute FileBoardVO board, @RequestPart MultipartFile files,
				HttpServletRequest request) throws IllegalStateException, IOException, Exception {
		
		if(files.isEmpty()) {
			fileBoardService.createFileBoard(board);
		}
		else {
			String file_name = files.getOriginalFilename();
			
			String fileNameExtension = FilenameUtils.getExtension(file_name).toLowerCase();
			File destinationFile;
			String destinationFileName;
			
			String fileUrl = "/Users/bitnarikim/Downloads/";
			
			do {
				destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + fileNameExtension;
				destinationFile = new File(fileUrl + destinationFileName);
			} while (destinationFile.exists());
			
			destinationFile.getParentFile().mkdirs();
			files.transferTo(destinationFile);
			
			fileBoardService.createFileBoard(board);
			
			FileVO fileVO = new FileVO();
			fileVO.setB_no(board.getB_no());
			fileVO.setFile_name(destinationFileName);
			fileVO.setFile_origin_name(file_name);
			fileVO.setFile_url(fileUrl);
			
			fileBoardService.createFile(fileVO);
		}
		
		return "forward:/fileBoard/list"; //객체 재사용
	}
	
	
	//Read
	@RequestMapping("/list")
	private String fileBoardList(Model model, HttpServletRequest request) {
		
		List<FileBoardVO> testList = new ArrayList<>();
		testList = fileBoardService.readFileBoardList();
		
		for(int i=0;i<testList.size();i++) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String nowDate = simpleDateFormat.format(testList.get(i).getReg_date());
			testList.get(i).setNow_date(nowDate);
		}
		
		model.addAttribute("testlist", testList);
		
		return "fileBoard/list";
	}
	@RequestMapping("/detail/{b_no}")
	private String fileBoardDetail(@PathVariable("b_no") int b_no, Model model) {
		FileBoardVO boardDetail = fileBoardService.readFileBoardDetail(b_no);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = simpleDateFormat.format(boardDetail.getReg_date());
		boardDetail.setNow_date(nowDate);

		model.addAttribute("detail", boardDetail);
		
		if(fileBoardService.readFileDetail(b_no) == null) return "fileBoard/detail";
		else {
			model.addAttribute("file", fileBoardService.readFileDetail(b_no));
			return "fileBoard/detail"; 
		}
	}
	@RequestMapping("/fileDown/{b_no}")
	private void fileDown(@PathVariable("b_no") int b_no, HttpServletRequest request, 
			HttpServletResponse response) throws UnsupportedEncodingException, Exception {
		request.setCharacterEncoding("UTF-8");
		FileVO fileVO = fileBoardService.readFileDetail(b_no);
		
		try {
			String fileUrl = fileVO.getFile_url();
			System.out.println(fileUrl);
			//fileUrl += "/";
			String savePath = fileUrl;
			String fileName = fileVO.getFile_name();
			
			String originFileName = fileVO.getFile_origin_name();
			InputStream in = null;
			OutputStream out = null;
			File file = null;
			Boolean skip = false;
			String client = "";
			
			try {
				file = new File(savePath, fileName);
				in = new FileInputStream(file);
			} catch (FileNotFoundException fe) {
				skip = true;
			}
			
			client = request.getHeader("User-Agent");
			
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Description", "HTML Generated Data");
			
			if(!skip) {
				if(client.indexOf("MSIE") != -1)
					response.setHeader("Content-Description", "attachment; filename=\""+java.net.URLEncoder.encode(originFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
				else  if(client.indexOf("Trident") != -1)
					response.setHeader("Content-Description", "attachment; filename=\""+java.net.URLEncoder.encode(originFileName, "UTF-8").replaceAll("\\+", "\\ ") + "\"");
				else {
					response.setHeader("Content-Description", "attachment; filename=\""+ new String(originFileName.getBytes("UTF-8"), "ISO8859_1") + "\"");
					response.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
				}
				
				response.setHeader("Content-Length", ""+file.length());
				out = response.getOutputStream();
				byte b[] = new byte[(int) file.length()];
				int leng = 0;
				
				while((leng = in.read(b)) > 0) {
					out.write(b, 0, leng);
				}
			} else {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter pw = response.getWriter();
				pw.println("<script> alert('파일을 찾을 수 없습니다.'); history.back(); </script>");
				pw.flush();
			}
			
			in.close();
			out.close();
		} catch (Exception e) {
			System.out.println("ERROR : " + e.getStackTrace());
		}
	}
	
	
	//Update
	@RequestMapping("/update/{b_no}")
	private String fileBoardUpdateForm(@PathVariable("b_no") int b_no, Model model) {
		model.addAttribute("detail", fileBoardService.readFileBoardDetail(b_no));
		
		return "fileBoard/update";
	}
	@RequestMapping("/updateProc")
	private String fileBoardUpdateProc(@ModelAttribute FileBoardVO board) {
		fileBoardService.updateFileBoard(board);
		int bno = board.getB_no();
		String b_no = Integer.toString(bno);
		
		return "redirect:/fileBoard/detail/"+b_no;
	}
	
	
	//Delete
	@RequestMapping("/delete/{b_no}")
	private String fileBoardDelete(@PathVariable("b_no") int b_no) {
		fileBoardService.deleteFileBoard(b_no);
		
		return "redirect:/fileBoard/list";
	}
	
}
