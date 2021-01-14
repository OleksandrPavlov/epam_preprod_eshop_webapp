package com.epam.preprod.pavlov.service.additional.impl;

import com.epam.preprod.pavlov.exception.ApplicationException;
import com.epam.preprod.pavlov.exception.AvatarServiceException;
import com.epam.preprod.pavlov.service.additional.AvatarService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Part;
import java.io.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AvatarServiceImpl implements AvatarService {
    public static final Logger AVATAR_SERVICE_LOGGER = LoggerFactory.getLogger(AvatarService.class);
    public static final String PART_CONTENT_DISPOSITION_HEADER = "content-disposition";
    public static final String DEFAULT_AVATAR_FOLDER = File.pathSeparator + "avatars";
    public static final String POINT_COMMA = ";";
    public static final String FILENAME_HEADER_PATTERN = "^filename=\"[a-zA-ZА-Яа-я0-9ЁёЇї]+\\.(?<extension>[a-z]+)\"$";
    public static final String EXTENSION_GROUP_NAME = "extension";
    public static final String DEFAULT_AVATAR_NAME = "def-ava.png";

    private File avatarDirectory;
    private File defaultAvatarFile;

    public AvatarServiceImpl(String avatarFolder, String realPath) {
        this.avatarDirectory = getAvatarFolder(avatarFolder);
        this.defaultAvatarFile = new File(realPath + DEFAULT_AVATAR_NAME);
    }

    @Override
    public String saveAvatar(Part avatarPart) {
        String contentDisposition = avatarPart.getHeader(PART_CONTENT_DISPOSITION_HEADER);
        String extension = extractExtension(contentDisposition);
        if (StringUtils.isBlank(extension)) {
            return DEFAULT_AVATAR_NAME;
        }
        String uniqueFileName = generateUniqueFileNameInAvatarDirectory(extension);
        try (OutputStream output = new FileOutputStream(avatarDirectory.getAbsolutePath() + File.separator + uniqueFileName)) {
            InputStream inputStream = avatarPart.getInputStream();
            int b;
            while ((b = inputStream.read()) != -1) {
                output.write(b);
            }
        } catch (IOException exception) {
            AVATAR_SERVICE_LOGGER.error("AvatarServiceException was dropped during saving of avatar due to IOException occurring!");
            throw new AvatarServiceException("Avatar has not been saved");
        }
        return uniqueFileName;
    }

    @Override
    public boolean deleteAvatar(String avatarName) {
        File[] files = avatarDirectory.listFiles();
        if (files.length == 0) {
            return false;
        }
        File file = Arrays.stream(files).filter(value -> value.getName().equals(avatarName)).findFirst().get();
        return file.delete();
    }

    @Override
    public String getPathToAvatar(String pictureName) {
        return avatarDirectory.getAbsolutePath() + File.separator + pictureName;
    }

    @Override
    public byte[] getImageAsByte(String pictureName) {
        File avatarFile = getAvatarFile(pictureName);
        byte[] bytes;
        try (InputStream is = new FileInputStream(avatarFile)) {
            bytes = new byte[is.available()];
            is.read(bytes);
        } catch (IOException ex) {
            AVATAR_SERVICE_LOGGER.error("AvatarServiceException was dropped during getting of avatar byte representation due to IOException occurring!");
            throw new AvatarServiceException("Avatar has not been translated into bytes");
        }
        return bytes;
    }

    private String generateUniqueFileNameInAvatarDirectory(String extension) {
        Random random = new Random(System.currentTimeMillis());
        File[] files = avatarDirectory.listFiles();
        if (Objects.isNull(files)) {
            AVATAR_SERVICE_LOGGER.error("AvatarServiceException was dropped during generating unique file name!");
            throw new AvatarServiceException("Your directory isn't valid or someone else!");
        }
        String prefix = "ava_";
        String generatedName = StringUtils.EMPTY;
        boolean coincidenceFounded = true;
        while (coincidenceFounded) {
            coincidenceFounded = false;
            generatedName = prefix + (Math.abs(random.nextInt())) + "." + extension;
            for (File file : files) {
                if (file.getName().equals(generatedName)) {
                    coincidenceFounded = true;
                }
            }
        }
        return generatedName;
    }

    private String extractExtension(String contentDisposition) {
        int numberOfContentHeaderPosition = 2;
        String extension = "";
        String[] headers = contentDisposition.split(POINT_COMMA);
        String fileName = headers[numberOfContentHeaderPosition];
        fileName = fileName.trim();
        Pattern pattern = Pattern.compile(FILENAME_HEADER_PATTERN);
        Matcher m = pattern.matcher(fileName);
        if (m.find()) {
            extension = m.group(EXTENSION_GROUP_NAME);
        }
        return extension;
    }

    private File getDefaultAvatarFolder() throws ApplicationException {
        File file = new File(DEFAULT_AVATAR_FOLDER);
        if (file.exists()) {
            return file;
        }
        ApplicationException createFolderException = new ApplicationException("Avatar folder has not been created!");
        boolean isCreated;
        isCreated = file.mkdir();
        if (!isCreated) {
            AVATAR_SERVICE_LOGGER.error("Default avatar folder has not been created, check you system security manager!");
            throw createFolderException;
        }
        return file;
    }

    public File getAvatarFile(String avatarName) {
        File avatarFile = new File(avatarDirectory + File.separator + avatarName);
        return avatarFile.exists() ? avatarFile : defaultAvatarFile;
    }

    private File getAvatarFolder(String customAvatarFolder) {
        File file;
        if (Objects.isNull(customAvatarFolder)) {
            return getDefaultAvatarFolder();
        }
        file = new File(customAvatarFolder);
        if (!file.isDirectory()) {
            boolean isCreated;
            isCreated = file.mkdir();
            if (isCreated && file.isDirectory()) {
                AVATAR_SERVICE_LOGGER.debug("Specified avatar folder does not exist. The folder has been created: " + file.getAbsolutePath());
            } else {
                AVATAR_SERVICE_LOGGER.error("Specified avatar file path does not exist or it is not a directory! Check this file: " + file.getAbsolutePath());
                file = getDefaultAvatarFolder();
                AVATAR_SERVICE_LOGGER.debug("Avatar file path adjusted as: " + file.getAbsolutePath());
            }
        }
        return file;
    }

}
