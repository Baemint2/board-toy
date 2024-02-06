package org.example.board.domain.file;

public class FileUpload {
    //        try{
//            String origFileName = files.getOriginalFilename();
//            String filename = new MD5Generator(origFileName).toString();
//
//            String savePath = System.getProperty("user.dir") + "\\files";
//
//            if(!new File(savePath).exists()) {
//                try {
//                    new File(savePath).mkdir();
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            String filePath = savePath + "\\" + filename;
//            files.transferTo(new File(filePath));
//
//            FileDto fileDto = new FileDto();
//            fileDto.setOrigFilename(origFileName);
//            fileDto.setFilename(filename);
//            fileDto.setFilePath(filePath);
//
//            Long fileId = fileService.saveFile(fileDto);
//            postsSaveRequestDto.setFileId(fileId);
//            postsService.save(postsSaveRequestDto);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
}
