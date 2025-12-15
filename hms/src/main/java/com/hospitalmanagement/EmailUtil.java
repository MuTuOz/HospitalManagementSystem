package com.hospitalmanagement;

public class EmailUtil {
    // Lightweight stub: logs email attempts. If you want real SMTP, replace with a proper
    // Jakarta Mail implementation and add dependency to pom.xml.
    public static boolean sendEmail(String to, String subject, String body) {
        System.out.println("[EmailUtil] sendEmail to=" + to + " subject=" + subject + " body=" + body);
        return false;
    }

    public static void sendAppointmentNotifications(String patientEmail, String doctorEmail, String details) {
        sendEmail(patientEmail, "Randevu Onayı", "Randevunuz onaylandı:\n" + details);
        sendEmail(doctorEmail, "Yeni Randevu", "Yeni bir randevu oluşturuldu:\n" + details);
    }
}
