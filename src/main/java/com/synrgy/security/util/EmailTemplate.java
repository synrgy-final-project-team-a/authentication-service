package com.synrgy.security.util;

import org.springframework.stereotype.Component;

@Component("emailTemplate")
public class EmailTemplate {
    public String getResetPassword(){
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "\t.email-container {\n" +
                "\t\tpadding-top: 10px;\n" +
                "\t}\n" +
                "\tp {\n" +
                "\t\ttext-align: left;\n" +
                "\t}\n" +
                "\n" +
                "\ta.btn {\n" +
                "\t\tdisplay: block;\n" +
                "\t\tmargin: 30px auto;\n" +
                "\t\tbackground-color: #01c853;\n" +
                "\t\tpadding: 10px 20px;\n" +
                "\t\tcolor: #fff;\n" +
                "\t\ttext-decoration: none;\n" +
                "\t\twidth: 30%;\n" +
                "\t\ttext-align: center;\n" +
                "\t\tborder: 1px solid #01c853;\n" +
                "\t\ttext-transform: uppercase;\n" +
                "\t}\n" +
                "\ta.btn:hover,\n" +
                "\ta.btn:focus {\n" +
                "\t\tcolor: #01c853;\n" +
                "\t\tbackground-color: #fff;\n" +
                "\t\tborder: 1px solid #01c853;\n" +
                "\t}\n" +
                "\t.user-name {\n" +
                "\t\ttext-transform: uppercase;\n" +
                "\t}\n" +
                "\t.manual-link,\n" +
                "\t.manual-link:hover,\n" +
                "\t.manual-link:focus {\n" +
                "\t\tdisplay: block;\n" +
                "\t\tcolor: #396fad;\n" +
                "\t\tfont-weight: bold;\n" +
                "\t\tmargin-top: -15px;\n" +
                "\t}\n" +
                "\t.mt--15 {\n" +
                "\t\tmargin-top: -15px;\n" +
                "\t}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<div class=\"email-container\">\n" +
                "\t\t<p>Jika kamu melakukan request untuk mengganti password dengan username {{USERNAME}}, silahkan klik link di bawah ini untuk proses lebih lanjut. Jika Anda sudah menerima pesan ini lebih dari satu kali atau sudah mengklik link, silahkan abaikan pesan ini.</p>\n" +
                "\t\t\n" +
                "\t\tLink: <b>{{PASS_TOKEN}}</b>\n" +
                "\t\t\n" +
                "\t\t\n" +
                "\t\t<p>Best regards,</p>\n" +
                "\t\t<p>Kosanku</p>\n" +
                "\t\t<p class=\"mt--15\".....</p>\n" +
                "\n" +
                "\t\t\n" +
                "\t</div>\n" +
                "</body>\n" +
                "</html>";
    }

    public String getRegisterTemplate() {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "\t.email-container {\n" +
                "\t\tpadding-top: 10px;\n" +
                "\t}\n" +
                "\tp {\n" +
                "\t\ttext-align: left;\n" +
                "\t}\n" +
                "\n" +
                "\ta.btn {\n" +
                "\t\tdisplay: block;\n" +
                "\t\tmargin: 30px auto;\n" +
                "\t\tbackground-color: #01c853;\n" +
                "\t\tpadding: 10px 20px;\n" +
                "\t\tcolor: #fff;\n" +
                "\t\ttext-decoration: none;\n" +
                "\t\twidth: 30%;\n" +
                "\t\ttext-align: center;\n" +
                "\t\tborder: 1px solid #01c853;\n" +
                "\t\ttext-transform: uppercase;\n" +
                "\t}\n" +
                "\ta.btn:hover,\n" +
                "\ta.btn:focus {\n" +
                "\t\tcolor: #01c853;\n" +
                "\t\tbackground-color: #fff;\n" +
                "\t\tborder: 1px solid #01c853;\n" +
                "\t}\n" +
                "\t.user-name {\n" +
                "\t\ttext-transform: uppercase;\n" +
                "\t}\n" +
                "\t.manual-link,\n" +
                "\t.manual-link:hover,\n" +
                "\t.manual-link:focus {\n" +
                "\t\tdisplay: block;\n" +
                "\t\tcolor: #396fad;\n" +
                "\t\tfont-weight: bold;\n" +
                "\t\tmargin-top: -15px;\n" +
                "\t}\n" +
                "\t.mt--15 {\n" +
                "\t\tmargin-top: -15px;\n" +
                "\t}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<div class=\"email-container\">\n" +
                "\t\t<p>Halo <span class=\"user-name\">{{USERNAME}}</span> Selamat bergabung di Kosanku!</p>\n" +
                "\t\t<p>Harap konfirmasikan email kamu dengan mengklik link dibawah ini</p>\n" +
                "\t\t\n" +
                "\t\tLink: <b>{{VERIFY_TOKEN}}</b>\n" +
                "\t\t\n" +
                "\t\t<p class=\"mt--15\">Jika kamu butuh bantuan atau pertanyaan, silahkan hubungi customer service kami di 085155050220 atau kirim email ke admin@kosanku.com</p>\n" +
                "\t\t\n" +
                "\t\t<p>Semoga harimu menyenangkan seperti admin Kosanku (^-^)</p>\n" +
                "\t\t\n" +
                "\t\t<p>Kosanku</p>\n" +
                "\t\t<p class=\"mt--15\".....</p>\n" +
                "\n" +
                "\t\t\n" +
                "\t</div>\n" +
                "</body>\n" +
                "</html>";
    }

}
