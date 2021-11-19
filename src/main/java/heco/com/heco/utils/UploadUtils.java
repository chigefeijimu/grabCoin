package heco.com.heco.utils;

import com.alibaba.fastjson.JSONObject;
import com.edoc2.file.FileUtils;
import com.edoc2.model.ResultBean;
import com.edoc2.model.upload.CheckRegionData;
import com.edoc2.model.upload.CheckRegionResult;
import com.edoc2.model.upload.UploadFileInfo;
import com.edoc2.model.upload.UploadFileResult;
import com.edoc2.model.upload.uploadenum.CalcMD5;
import com.edoc2.model.upload.uploadenum.UpgradeStrategy;
import com.edoc2.sdkconfig.SdkBaseInfo;
import com.edoc2.utils.DateUtils;
import com.edoc2.utils.HttpUtils;
import com.edoc2.utils.Md5ResultEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.*;

public class UploadUtils {
    private static int headMd5Size = 1048576;
    private static String baseUploadUrl = "/document/upload?token=";
    private static String checkRegionUrl = "/WebCore";
    private static String fileInfoSeparateChar = String.valueOf('\u0001');
    private static List<UpgradeStrategy> updateStrategyList = new ArrayList();
    public static int chunkSize = 5242880;

    public UploadUtils() {
    }

    public static ResultBean uploadFileByBufferedInputStream(String token, int parentFolderId, UpgradeStrategy upgradeStrategy, String calcMd5, String newFileName, BufferedInputStream bufferedInputStream, UploadFileInfo uploadFileInfo, long fileSize) throws IOException {
        String uploadUrl = SdkBaseInfo.baseUrl;
        if (upgradeStrategy == null) {
            upgradeStrategy = UpgradeStrategy.Skip;
        }

        if (StringUtils.isBlank(calcMd5)) {
            calcMd5 = CalcMD5.None.getValue();
        }

        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("token不能为空...");
        } else if (upgradeStrategy == UpgradeStrategy.Rename && StringUtils.isBlank(newFileName)) {
            throw new RuntimeException("当重名策略设置为重命名时，新的文件名称（newFileName）不能为空...");
        } else {
            if (StringUtils.isNotBlank(newFileName)) {
                uploadFileInfo.setName(newFileName);
            }

            uploadFileInfo.setSize(fileSize);
            setUploadFileInfoByStreamInit(uploadFileInfo, parentFolderId);
            ResultBean regionResult = uploadFileByFileInputStream(token, upgradeStrategy, calcMd5, newFileName, uploadUrl, fileSize, uploadFileInfo, bufferedInputStream);

            try {
                if (Files.exists(Paths.get(uploadFileInfo.getName()), new LinkOption[0])) {
                    Files.delete(Paths.get(uploadFileInfo.getName()));
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

            return regionResult != null ? regionResult : new ResultBean(false, 0, "上传失败...");
        }
    }

    public static ResultBean uploadFile(String token, String path, int parentFolderId, UpgradeStrategy upgradeStrategy, String calcMd5, String newFileName, String fullPath, int fileId) {
        String uploadUrl = SdkBaseInfo.baseUrl;
        if (upgradeStrategy == null) {
            upgradeStrategy = UpgradeStrategy.Skip;
        }

        if (StringUtils.isBlank(calcMd5)) {
            calcMd5 = CalcMD5.None.getValue();
        }

        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("token不能为空...");
        } else if (upgradeStrategy == UpgradeStrategy.Rename && StringUtils.isBlank(newFileName)) {
            throw new RuntimeException("当重名策略设置为重命名时，新的文件名称（newFileName）不能为空...");
        } else {
            File currentFile = new File(path);
            if (currentFile == null) {
                throw new RuntimeException("当前文件不存在！");
            } else {
                UploadFileInfo uploadFileInfo = new UploadFileInfo();
                if (fileId > 0) {
                    uploadFileInfo.setFileId(fileId);
                }

                setUploadFileInfoInit(path, parentFolderId, newFileName, fullPath, currentFile, uploadFileInfo, upgradeStrategy);
                BufferedInputStream bufferedInputStream = null;

                try {
                    FileInputStream fileInputStream = new FileInputStream(currentFile);
                    bufferedInputStream = new BufferedInputStream(fileInputStream);
                    if (bufferedInputStream == null) {
                        throw new RuntimeException("转文件流（bufferedInputStream）失败...");
                    }
                } catch (FileNotFoundException var15) {
                    throw new RuntimeException("转文件流（fileInputStream）失败...");
                }

                ResultBean regionResult = uploadFileByFileInputStream(token, upgradeStrategy, calcMd5, newFileName, uploadUrl, currentFile.length(), uploadFileInfo, bufferedInputStream);

                try {
                    if (Files.exists(Paths.get(uploadFileInfo.getName()), new LinkOption[0])) {
                        Files.delete(Paths.get(uploadFileInfo.getName()));
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

                return regionResult != null ? regionResult : new ResultBean(false, 0, "上传失败...");
            }
        }
    }

    public static ResultBean uploadFile(String token, String path, int parentFolderId, UpgradeStrategy upgradeStrategy, String calcMd5, String newFileName, String fullPath, int fileId, String fileRemark) {
        String uploadUrl = SdkBaseInfo.baseUrl;
        if (upgradeStrategy == null) {
            upgradeStrategy = UpgradeStrategy.Skip;
        }

        if (StringUtils.isBlank(calcMd5)) {
            calcMd5 = CalcMD5.None.getValue();
        }

        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("token不能为空...");
        } else if (upgradeStrategy == UpgradeStrategy.Rename && StringUtils.isBlank(newFileName)) {
            throw new RuntimeException("当重名策略设置为重命名时，新的文件名称（newFileName）不能为空...");
        } else {
            File currentFile = new File(path);
            if (currentFile == null) {
                throw new RuntimeException("当前文件不存在！");
            } else {
                UploadFileInfo uploadFileInfo = new UploadFileInfo();
                uploadFileInfo.setFileRemark(fileRemark);
                if (fileId > 0) {
                    uploadFileInfo.setFileId(fileId);
                }

                setUploadFileInfoInit(path, parentFolderId, newFileName, fullPath, currentFile, uploadFileInfo, upgradeStrategy);
                BufferedInputStream bufferedInputStream = null;

                try {
                    FileInputStream fileInputStream = new FileInputStream(currentFile);
                    bufferedInputStream = new BufferedInputStream(fileInputStream);
                    if (bufferedInputStream == null) {
                        throw new RuntimeException("转文件流（bufferedInputStream）失败...");
                    }
                } catch (FileNotFoundException var16) {
                    throw new RuntimeException("转文件流（fileInputStream）失败...");
                }

                ResultBean regionResult = uploadFileByFileInputStream(token, upgradeStrategy, calcMd5, newFileName, uploadUrl, currentFile.length(), uploadFileInfo, bufferedInputStream);

                try {
                    if (Files.exists(Paths.get(uploadFileInfo.getName()), new LinkOption[0])) {
                        Files.delete(Paths.get(uploadFileInfo.getName()));
                    }
                } catch (IOException var15) {
                    var15.printStackTrace();
                }

                return regionResult != null ? regionResult : new ResultBean(false, 0, "上传失败...");
            }
        }
    }

    public static ResultBean uploadFileByStream(String token, int parentFolderId, UpgradeStrategy upgradeStrategy, String calcMd5, String newFileName, FileInputStream stream, UploadFileInfo uploadFileInfo) throws IOException {
        String uploadUrl = SdkBaseInfo.baseUrl;
        if (upgradeStrategy == null) {
            upgradeStrategy = UpgradeStrategy.Skip;
        }

        if (StringUtils.isBlank(calcMd5)) {
            calcMd5 = CalcMD5.None.getValue();
        }

        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("token不能为空...");
        } else if (upgradeStrategy == UpgradeStrategy.Rename && StringUtils.isBlank(newFileName)) {
            throw new RuntimeException("当重名策略设置为重命名时，新的文件名称（newFileName）不能为空...");
        } else {
            long fileSize = stream.getChannel().size();
            if (upgradeStrategy == UpgradeStrategy.Rename && StringUtils.isNotBlank(newFileName)) {
                uploadFileInfo.setName(newFileName);
            }

            uploadFileInfo.setSize(fileSize);
            setUploadFileInfoByStreamInit(uploadFileInfo, parentFolderId);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
            ResultBean regionResult = uploadFileByFileInputStream(token, upgradeStrategy, calcMd5, newFileName, uploadUrl, fileSize, uploadFileInfo, bufferedInputStream);

            try {
                if (Files.exists(Paths.get(uploadFileInfo.getName()), new LinkOption[0])) {
                    Files.delete(Paths.get(uploadFileInfo.getName()));
                }
            } catch (IOException var13) {
                var13.printStackTrace();
            }

            return regionResult != null ? regionResult : new ResultBean(false, 0, "上传失败...");
        }
    }

    private static void setUploadFileInfoByStreamInit(UploadFileInfo uploadFileInfo, int parentFolderId) {
        uploadFileInfo.setChunkSize(chunkSize);
        uploadFileInfo.setParentFolderId(parentFolderId);
        uploadFileInfo.setHash(uploadFileInfo.getId());
        uploadFileInfo.setUploadId(uploadFileInfo.getId());
        uploadFileInfo.setChunks((int)Math.ceil((double)uploadFileInfo.getSize() / ((double)uploadFileInfo.getChunkSize() * 1.0D)));
        List<Object> list = new ArrayList();
        list.add(parentFolderId);
        list.add(uploadFileInfo.getName());
        list.add("");
        list.add("");
        list.add("0");
        list.add("");
        String join = StringUtils.join(list, "1");
        uploadFileInfo.setFileInfo(join);
        if (uploadFileInfo.getChunks() <= 0) {
            uploadFileInfo.setChunks(1);
        }

    }

    private static ResultBean uploadFileByFileInputStream(String token, UpgradeStrategy upgradeStrategy, String calcMd5, String newFileName, String uploadUrl, long fileSize, UploadFileInfo uploadFileInfo, BufferedInputStream bufferedInputStream) {
        updateUploadFileInfo(upgradeStrategy, newFileName, uploadFileInfo);
        CheckRegionResult regionResult = getCheckRegionResult(token, upgradeStrategy, uploadUrl, uploadFileInfo, newFileName);
        if (regionResult.getResult() == 610) {
            return new ResultBean(false, regionResult.getResult(), regionResult.getReason(), regionResult);
        } else if (regionResult.getResult() != 0) {
            throw new RuntimeException("获取分区域信息失败..." + regionResult.getReason());
        } else if (regionResult.getResult() == 0 && fileSize == 0L) {
            UploadFileResult uploadFileResult = new UploadFileResult(regionResult.getData());
            return new ResultBean(true, 1, "上传0kb的文件成功", uploadFileResult);
        } else {
            CheckRegionData checkRegionData = regionResult.getData();
            if (checkRegionData != null) {
                uploadFileInfo.setRegionHash(checkRegionData.getRegionHash());
                uploadFileInfo.setRegionId(checkRegionData.getRegionId());
            }

            if (checkRegionData != null && StringUtils.isNotBlank(checkRegionData.getRegionUrl())) {
                uploadUrl = checkRegionData.getRegionUrl();
            }

            String fileMd5 = "";

            try {
                if (uploadFileInfo.getChunks() <= 1) {
                    calcMd5 = CalcMD5.None.getValue();
                }

                byte var13 = -1;
                switch(calcMd5.hashCode()) {
                    case -1051894239:
                        if (calcMd5.equals("Uploading")) {
                            var13 = 2;
                        }
                        break;
                    case 2433880:
                        if (calcMd5.equals("None")) {
                            var13 = 0;
                        }
                        break;
                    case 1985948575:
                        if (calcMd5.equals("Before")) {
                            var13 = 1;
                        }
                }

                String cookieStr = "";
                switch(var13) {
                    case 1:
                        Md5ResultEntity md5ResultEntityOld = FileUtils.getMD5(bufferedInputStream, headMd5Size, uploadFileInfo.getName());
                        bufferedInputStream = md5ResultEntityOld.getInputStream();
                        Map<String, String> params = new HashMap();
                        params.put("module", "UploadFileManager");
                        params.put("fun", "IsExistsHeadMd5");
                        params.put("token", token);
                        params.put("headMd5", md5ResultEntityOld.getMd5());
                        params.put("areaId", uploadFileInfo.getRegionId() + "");
                        cookieStr = HttpUtils.doPost(SdkBaseInfo.baseUrl + "/WebCore", params);
                        if (StringUtils.isBlank(cookieStr) && Integer.parseInt(JSONObject.parseObject(cookieStr).get("result").toString()) != 0) {
                            throw new RuntimeException("根据MD5值获取是否分区域失败...");
                        } else {
                            boolean isExistsHeadMd5Async = (Boolean)JSONObject.parseObject(cookieStr).get("exists");
                            if (isExistsHeadMd5Async) {
                                Md5ResultEntity md5ResultEntity = FileUtils.getMD5Three(bufferedInputStream);
                                bufferedInputStream = md5ResultEntity.getInputStream();
                                fileMd5 = md5ResultEntity.getMd5();
                            }
                        }
                    case 0:
                    case 2:
                    default:
                        String uploadFileBlockUrl = uploadUrl + baseUploadUrl;
                        boolean isBlocking = false;
                        if (fileSize > (long)chunkSize) {
                            isBlocking = true;
                        }

                        String resultStr = "";
                        if (isBlocking) {
                            int blockNum = uploadFileInfo.getChunks();
                            int chunk = uploadFileInfo.getChunk();
                            int currentBlockSite = 0;

                            while(chunk < blockNum) {
                                if (blockNum > 1) {
                                    long uploadedSize = (long)(chunk * chunkSize);
                                    long bufferLength = Math.min((long)chunkSize, fileSize - uploadedSize);
                                    byte[] buffer = new byte[(int)bufferLength];
                                    bufferedInputStream.read(buffer, 0, (int)bufferLength);
                                    bufferedInputStream.mark(currentBlockSite);
                                    bufferedInputStream.reset();
                                    MultipartEntity multipartEntity = setMultipartEntity(uploadFileInfo, fileMd5, fileSize, buffer, isBlocking, (BufferedInputStream)null);
                                    currentBlockSite += buffer.length;
                                    resultStr = HttpUtils.httpPost(uploadFileBlockUrl + token, multipartEntity, cookieStr);
                                    ++chunk;
                                    uploadFileInfo.setChunk(uploadFileInfo.getChunk() + 1);
                                }
                            }
                        } else {
                            MultipartEntity multipartEntity = setMultipartEntity(uploadFileInfo, fileMd5, fileSize, (byte[])null, isBlocking, bufferedInputStream);
                            cookieStr = "";
                            resultStr = HttpUtils.httpPost(uploadFileBlockUrl + token, multipartEntity, cookieStr);
                        }

                        bufferedInputStream.close();
                        if (StringUtils.isNotBlank(resultStr)) {
                            UploadFileResult result = (UploadFileResult)JSONObject.parseObject(resultStr, UploadFileResult.class);
                            if (result != null && !"Error".equals(result.getStatus())) {
                                result.setFileInfo(checkRegionData);
                                return new ResultBean(true, 1, "上传成功...", result);
                            }
                        }

                        return null;
                }
            } catch (Exception var25) {
                var25.printStackTrace();
                throw new RuntimeException("上传报错了..." + var25.getMessage());
            }
        }
    }

    private static CheckRegionResult getCheckRegionResult(String token, UpgradeStrategy upgradeStrategy, String uploadUrl, UploadFileInfo uploadFileInfo, String newFileName) {
        Map<String, String> checkRegionParams = setCheckRegionParams(token, upgradeStrategy, uploadFileInfo);
        String checkReUrl = uploadUrl + checkRegionUrl;
        String message = null;

        try {
            message = HttpUtils.doPost(checkReUrl, checkRegionParams);
        } catch (Exception var9) {
            var9.printStackTrace();
            throw new RuntimeException("获取分区域信息失败..." + var9.getMessage());
        }

        if (StringUtils.isBlank(message)) {
            throw new RuntimeException("获取分区域信息失败...");
        } else {
            return (CheckRegionResult)JSONObject.parseObject(message, CheckRegionResult.class);
        }
    }

    private static MultipartEntity setMultipartEntity(UploadFileInfo uploadFileInfo, String fileMd5, long fileSize, byte[] buffer, boolean isBlocking, BufferedInputStream bufferedInputStream) throws UnsupportedEncodingException {
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, "----WebKitFormBoundarymx2fSWqWSd0OxQqq", Charset.defaultCharset());
        multipartEntity.addPart("id", new StringBody(uploadFileInfo.getId(), StandardCharsets.UTF_8));
        multipartEntity.addPart("name", new StringBody(uploadFileInfo.getName(), StandardCharsets.UTF_8));
        multipartEntity.addPart("type", new StringBody(uploadFileInfo.getType(), StandardCharsets.UTF_8));
        multipartEntity.addPart("lastModifiedDate", new StringBody(DateUtils.getDateString(uploadFileInfo.getLastModifiedDate()), StandardCharsets.UTF_8));
        multipartEntity.addPart("size", new StringBody(Long.valueOf(fileSize).toString(), StandardCharsets.UTF_8));
        multipartEntity.addPart("FILE_MODE", new StringBody(uploadFileInfo.getFileMode(), StandardCharsets.UTF_8));
        multipartEntity.addPart("FILE_INFO", new StringBody(uploadFileInfo.getFileInfo(), StandardCharsets.UTF_8));
        multipartEntity.addPart("hash", new StringBody(uploadFileInfo.getHash(), StandardCharsets.UTF_8));
        multipartEntity.addPart("uploadId", new StringBody(uploadFileInfo.getUploadId(), StandardCharsets.UTF_8));
        if (StringUtils.isNotBlank(uploadFileInfo.getFullPath())) {
            multipartEntity.addPart("fullPath", new StringBody(uploadFileInfo.getFullPath(), StandardCharsets.UTF_8));
        }

        multipartEntity.addPart("chunkSize", new StringBody(Integer.valueOf(uploadFileInfo.getChunkSize()).toString(), StandardCharsets.UTF_8));
        multipartEntity.addPart("regionId", new StringBody(uploadFileInfo.getRegionId() == null ? "" : uploadFileInfo.getRegionId().toString(), StandardCharsets.UTF_8));
        multipartEntity.addPart("regionHash", new StringBody(uploadFileInfo.getRegionHash() == null ? "" : uploadFileInfo.getRegionHash(), StandardCharsets.UTF_8));
        if (!StringUtils.isBlank(fileMd5)) {
            multipartEntity.addPart("fileMd5", new StringBody(fileMd5 + ""));
        }

        if (isBlocking) {
            multipartEntity.addPart("chunks", new StringBody(Integer.valueOf(uploadFileInfo.getChunks()).toString(), StandardCharsets.UTF_8));
            multipartEntity.addPart("chunk", new StringBody(Integer.valueOf(uploadFileInfo.getChunk()).toString(), StandardCharsets.UTF_8));
            multipartEntity.addPart("blockSize", new StringBody(Long.valueOf((long)buffer.length).toString(), StandardCharsets.UTF_8));
            multipartEntity.addPart("file", new ByteArrayBody(buffer, uploadFileInfo.getName()));
        } else {
            multipartEntity.addPart("blockSize", new StringBody(Long.valueOf(uploadFileInfo.getSize()).toString(), StandardCharsets.UTF_8));
            multipartEntity.addPart("file", new InputStreamBody(bufferedInputStream, uploadFileInfo.getName()));
        }

        return multipartEntity;
    }

    private static void updateUploadFileInfo(UpgradeStrategy upgradeStrategy, String newFileName, UploadFileInfo uploadFileInfo) {
        uploadFileInfo.setUploadId(UUID.randomUUID().toString());
        ArrayList list3;
        String join3;
        if (0 < uploadFileInfo.getFileId()) {
            uploadFileInfo.setFileMode("UPDATE");
            list3 = new ArrayList();
            list3.add(uploadFileInfo.getParentFolderId());
            list3.add(uploadFileInfo.getName());
            list3.add("");
            list3.add("");
            list3.add("0");
            list3.add(upgradeStrategy.getDescription());
            join3 = StringUtils.join(list3, fileInfoSeparateChar);
            uploadFileInfo.setFileInfo(join3);
        } else {
            if (upgradeStrategy.getValue() == UpgradeStrategy.Rename.getValue()) {
                if (StringUtils.isBlank(newFileName)) {
                    throw new RuntimeException("当重名策略设置为重命名时，renameFileName不能为空");
                }

                newFileName = newFileName.replace(FileUtils.getFileType(newFileName), "") + FileUtils.getFileType(uploadFileInfo.getName());
            } else {
                newFileName = uploadFileInfo.getName();
            }

            uploadFileInfo.setFileMode("UPLOAD");
            list3 = new ArrayList();
            list3.add(uploadFileInfo.getParentFolderId());
            list3.add(newFileName);
            list3.add("");
            list3.add("");
            list3.add("0");
            list3.add("");
            join3 = StringUtils.join(list3, fileInfoSeparateChar);
            uploadFileInfo.setFileInfo(join3);
        }

    }

    private static Map<String, String> setCheckRegionParams(String token, UpgradeStrategy upgradeStrategy, UploadFileInfo uploadFileInfo) {
        Map<String, String> checkRegionParams = new HashMap();
        checkRegionParams.put("module", "RegionDocOperationApi");
        checkRegionParams.put("fun", "CheckAndCreateDocInfo");
        checkRegionParams.put("folderId", Integer.valueOf(uploadFileInfo.getParentFolderId()).toString());
        checkRegionParams.put("masterFileId", "");
        checkRegionParams.put("token", token);
        checkRegionParams.put("fileName", uploadFileInfo.getName());
        checkRegionParams.put("fileRemark", uploadFileInfo.getFileRemark());
        checkRegionParams.put("size", Long.valueOf(uploadFileInfo.getSize()).toString());
        checkRegionParams.put("type", uploadFileInfo.getType());
        checkRegionParams.put("attachType", "0");
        checkRegionParams.put("strategy", upgradeStrategy.getDescription());
        checkRegionParams.put("fileModel", updateStrategyList.contains(upgradeStrategy) ? "UPDATE" : "UPLOAD");
        if (StringUtils.isNotBlank(uploadFileInfo.getFullPath())) {
            checkRegionParams.put("fullPath", uploadFileInfo.getFullPath());
        }

        if (uploadFileInfo.getFileId() > 0) {
            checkRegionParams.put("fileId", String.valueOf(uploadFileInfo.getFileId()));
        }

        return checkRegionParams;
    }

    private static void setUploadFileInfoInit(String path, int parentFolderId, String newFileName, String fullPath, File currentFile, UploadFileInfo uploadFileInfo, UpgradeStrategy upgradeStrategy) {
        String mimeType = FileUtils.getMimeType(path);
        String fileName = currentFile.getName();
        if (StringUtils.isNotBlank(newFileName) && upgradeStrategy == UpgradeStrategy.Rename) {
            fileName = newFileName;
        }

        Date lastModifiedDate = FileUtils.getFileLastModifiedDate(currentFile);
        long size = currentFile.length();
        System.out.println("当前文件大小:" + size);
        uploadFileInfo.setName(fileName);
        uploadFileInfo.setChunkSize(chunkSize);
        uploadFileInfo.setParentFolderId(parentFolderId);
        uploadFileInfo.setType(mimeType);
        uploadFileInfo.setLastModifiedDate(lastModifiedDate);
        uploadFileInfo.setSize(size);
        uploadFileInfo.setHash(uploadFileInfo.getId());
        uploadFileInfo.setUploadId(uploadFileInfo.getId());
        uploadFileInfo.setChunks((int)Math.ceil((double)uploadFileInfo.getSize() / ((double)uploadFileInfo.getChunkSize() * 1.0D)));
        if (StringUtils.isNotBlank(fullPath)) {
            uploadFileInfo.setFullPath(fullPath);
        }

        if (uploadFileInfo.getChunks() <= 0) {
            uploadFileInfo.setChunks(1);
        }

    }

    static {
        updateStrategyList.add(UpgradeStrategy.MinorUpgrade);
        updateStrategyList.add(UpgradeStrategy.MajorUpgrade);
        updateStrategyList.add(UpgradeStrategy.OverlayLatestVersion);
    }
}
