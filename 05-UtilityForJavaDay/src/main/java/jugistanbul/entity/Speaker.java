package jugistanbul.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

@Entity
public class Speaker
{

    @Id
    private int id;
    private String name;
    private String title;
    private boolean approve;
    private boolean retracted;
    private String mail;
    private LocalDateTime updateTime;

    public Speaker(){}

    public Speaker(final int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }

    public boolean isRetracted() {
        return retracted;
    }

    public void setRetracted(boolean retracted) {
        this.retracted = retracted;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Speaker speaker = (Speaker) o;
        return id == speaker.id &&
                approve == speaker.approve &&
                retracted == speaker.retracted &&
                name.equals(speaker.name) &&
                title.equals(speaker.title) &&
                mail.equals(speaker.mail) &&
                updateTime.equals(speaker.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, approve, retracted, mail, updateTime);
    }
}
