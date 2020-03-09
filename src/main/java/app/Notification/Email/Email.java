package app.Notification.Email;

import java.util.Objects;

class Email {
    private String subject;
    private String text;
    private String senderEmail;
    private String receiverEmail;

    public Email(String subject, String text, String senderEmail, String receiverEmail) {
        this.subject = subject;
        this.text = text;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return getSubject().equals(email.getSubject()) &&
                getText().equals(email.getText()) &&
                getSenderEmail().equals(email.getSenderEmail()) &&
                getReceiverEmail().equals(email.getReceiverEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSubject(), getText(), getSenderEmail(), getReceiverEmail());
    }
}
