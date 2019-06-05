package org.mlmunozd.app.MessageDeliverySystem.Models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.mlmunozd.app.MessageDeliverySystem.Persistence.MessageDeliverySystemDatabase;

import java.util.List;
import java.util.Objects;

@Table(database = MessageDeliverySystemDatabase.class)
public class User extends BaseModel{

    @PrimaryKey(autoincrement = true)
    private int idUSer=0;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private int movil;

    @Column
    private String token_movil = "";

    /*
    * Implementations Singleton
    */
    private static User singletonUser;

    public User(){
    }

    public static User getInstance(){
        if(singletonUser == null){
            singletonUser = new User();
        }
        return singletonUser;
    }

    /* */

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, int movil) {
        this.email = email;
        this.password = password;
        this.movil = movil;
    }

    public User(String email, String password, String token_movil) {
        this.email = email;
        this.password = password;
        this.token_movil = token_movil;
    }

    public int getIdUSer() {
        return idUSer;
    }

    public void setIdUSer(int idUSer) {
        this.idUSer = idUSer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMovil() {
        return movil;
    }

    public void setMovil(int movil) {
        this.movil = movil;
    }

    public String getToken_movil() {
        return token_movil;
    }

    public void setToken_movil(String token_movil) {
        this.token_movil = token_movil;
    }

    public User getUser(String emailUser){
        User user = SQLite.select()
                .from(User.class)
                .where(User_Table.email.is(emailUser))
                .querySingle();
        return user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (idUSer != user.idUSer) return false;
        if (movil != user.movil) return false;
        if (!email.equals(user.email)) return false;
        return password.equals(user.password);
    }

}
