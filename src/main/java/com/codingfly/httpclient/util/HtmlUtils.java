package com.codingfly.httpclient.util;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class HtmlUtils {

    public static HtmlCompressor compressor = new HtmlCompressor();

    static {
        compressor.setEnabled(true);
        compressor.setCompressCss(true);
        compressor.setCompressJavaScript(true);
        compressor.setYuiJsPreserveAllSemiColons(true);
        compressor.setPreserveLineBreaks(false); // 设置单行输出
        compressor.setRemoveIntertagSpaces(true);
        compressor.setRemoveComments(true);
        compressor.setRemoveMultiSpaces(true);
    }

    public static void main(String[] args) {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <title>Layui</title>\n" +
                "  <meta name=\"renderer\" content=\"webkit\">\n" +
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n" +
                "  <link rel=\"stylesheet\" href=\"//res.layui.com/layui/dist/css/layui.css\"  media=\"all\">\n" +
                "  <!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->\n" +
                "</head>\n" +
                "<body>        \n" +
                "\n" +
                "  <div class=\"layui-row\">\n" +
                "    <div class=\"layui-col-md5\">\n" +
                "      <div class=\"layui-row grid-demo\">\n" +
                "        <div class=\"layui-col-md3\">\n" +
                "          <div class=\"grid-demo grid-demo-bg1\">内部列</div>\n" +
                "        </div>\n" +
                "        <div class=\"layui-col-md9\">\n" +
                "          <div class=\"grid-demo grid-demo-bg2\">内部列</div>\n" +
                "        </div>\n" +
                "        <div class=\"layui-col-md12\">\n" +
                "          <div class=\"grid-demo grid-demo-bg3\">内部列</div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <div class=\"layui-col-md7\">\n" +
                "      <div class=\"layui-row grid-demo grid-demo-bg1\">\n" +
                "        <div class=\"layui-col-md12\">\n" +
                "          <div class=\"grid-demo\">内部列</div>\n" +
                "        </div>\n" +
                "        <div class=\"layui-col-md9\">\n" +
                "          <div class=\"grid-demo grid-demo-bg2\">内部列</div>\n" +
                "        </div>\n" +
                "        <div class=\"layui-col-md3\">\n" +
                "          <div class=\"grid-demo grid-demo-bg3\">内部列</div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </div>\n" +
                " \n" +
                "          \n" +
                "<script src=\"//res.layui.com/layui/dist/layui.js\" charset=\"utf-8\"></script>\n" +
                "<!-- 注意：如果你直接复制所有代码到本地，上述 JS 路径需要改成你本地的 -->\n" +
                "<script>\n" +
                " \n" +
                "</script>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        String compressHtml = compressor.compress(html);
        System.out.println(compressHtml);
    }

}