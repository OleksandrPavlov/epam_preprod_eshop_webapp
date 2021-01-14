package com.epam.preprod.pavlov.tags;

import com.epam.preprod.pavlov.constant.ContextConstants;
import com.epam.preprod.pavlov.constant.RequestConstants;
import com.epam.preprod.pavlov.entity.CaptchaPack;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.HashMap;

public class CaptchaTag extends SimpleTagSupport {
    private String captchaId;

    public void setCaptchaId(String captchaId) {
        this.captchaId = captchaId;
    }

    @Override
    public void doTag() throws JspException, IOException {
        HashMap<String, CaptchaPack> captchaContextContainer = (HashMap<String, CaptchaPack>) getJspContext().getAttribute(ContextConstants.CAPTCHA_CONTAINER, PageContext.APPLICATION_SCOPE);
        CaptchaPack captchaPack;
        if (captchaContextContainer.isEmpty()) {
            captchaPack = (CaptchaPack) getJspContext().getAttribute(captchaId, PageContext.SESSION_SCOPE);
            drawCaptcha(captchaPack, false);
        } else {
            captchaPack = captchaContextContainer.get(captchaId);
            drawCaptcha(captchaPack, true);
        }
    }

    private void drawCaptcha(CaptchaPack captchaPack, boolean hiddenField) throws IOException {
        getJspContext().getOut().print("<img src=\"" + captchaPack.getUrlImage() + "\" class=\"picture\"");
        if (hiddenField) {
            getJspContext().getOut().print("<input type=\"text\" name=\"" + RequestConstants.CAPTCHA_ID_HIDDEN_FIELD + "\" value=\"" + getJspContext().getAttribute(RequestConstants.CAPTCHA_ID_HIDDEN_FIELD, PageContext.REQUEST_SCOPE) + "\"/>");
        }
    }

}
