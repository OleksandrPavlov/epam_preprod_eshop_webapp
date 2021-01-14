package com.epam.preprod.pavlov.service.additional.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

public class AvatarServiceImplTest {
    private static final String PATH_TO_PICTURE = "src/main/webapp/static/img/123.png";
    private static final String CONTENT_DISPOSITION_HEADER = "content-disposition";
    private static final String CONTENT_DISPOSITION_BODY = "form-data; name=\"fieldName\"; filename=\"filename.jpg\"";
    private AvatarServiceImpl avatarService;
    private File tempAvatarDirection;
    private Part part;
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Before
    public void init() throws IOException {
        tempAvatarDirection = folder.newFolder();
        avatarService = new AvatarServiceImpl(tempAvatarDirection.getCanonicalPath(),"");
        part = mock(Part.class);
    }

    @Test
    public void shouldSaveAnAvatarWithProperExtensionWhenSaveAvatarCalled() throws IOException {
        Path path= Paths.get(PATH_TO_PICTURE);
        InputStream inputStream=Files.newInputStream(path);
        expect(part.getHeader(CONTENT_DISPOSITION_HEADER)).andReturn(CONTENT_DISPOSITION_BODY);
        expect(part.getInputStream()).andReturn(inputStream);
        replay(part);
        avatarService.saveAvatar(part);
        File[] files = tempAvatarDirection.listFiles();
        assertEquals(1, files.length);
        verify(part);
    }

    @Test
    public void shouldDeleteAvatarWhenDeleteAvatarCalled() throws IOException {
        Path path= Paths.get(PATH_TO_PICTURE);
        InputStream inputStream=Files.newInputStream(path);
        expect(part.getHeader(CONTENT_DISPOSITION_HEADER)).andReturn(CONTENT_DISPOSITION_BODY);
        expect(part.getInputStream()).andReturn(inputStream);
        replay(part);
        String avatarName = avatarService.saveAvatar(part);
        File[] files = tempAvatarDirection.listFiles();
        assertEquals(1, files.length);
        avatarService.deleteAvatar(avatarName);
        File[] filesAfterDeleting = tempAvatarDirection.listFiles();
        assertEquals(0, filesAfterDeleting.length);
        verify(part);
    }


    @Test
    public void shouldReturnProperByteRepresentationWhenGetImageAsByteCalled() throws IOException {
        Path path= Paths.get(PATH_TO_PICTURE);
        InputStream inputStream=Files.newInputStream(path);
        expect(part.getHeader(CONTENT_DISPOSITION_HEADER)).andReturn(CONTENT_DISPOSITION_BODY);
        expect(part.getInputStream()).andReturn(inputStream);
        replay(part);
        String avatarName = avatarService.saveAvatar(part);
        byte[] bytes = avatarService.getImageAsByte(avatarName);
        File[] files = tempAvatarDirection.listFiles();
        assertEquals(files[0].length(), bytes.length);
    }
}

