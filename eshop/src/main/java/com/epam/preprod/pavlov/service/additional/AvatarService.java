package com.epam.preprod.pavlov.service.additional;

import javax.servlet.http.Part;

public interface AvatarService {
    /**
     * THis method saves avatar picture into specified folder. The path to folder initialised on creation stage.
     *
     * @param avatarPart All information extracting from this object
     * @return generated name in avatar folder
     */
    String saveAvatar(Part avatarPart);

    /**
     * This method removes avatar by "avatarName" and returns true in case of operation execution succeeded and false
     * in opposite case.
     *
     * @param avatarName name of avatar file
     * @return true or false depending on success.
     */
    boolean deleteAvatar(String avatarName);

    /**
     * This method returns full path to specified picture name
     *
     * @param pictureName This ic name of picture
     * @return full path
     */
    String getPathToAvatar(String pictureName);

    /**
     * This method return byte representation of picture.
     *
     * @param pictureName that will be translated
     * @return bytes
     */
    byte[] getImageAsByte(String pictureName);
}
