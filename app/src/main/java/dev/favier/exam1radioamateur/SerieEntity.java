package dev.favier.exam1radioamateur;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "series")
public class SerieEntity {
    @PrimaryKey
    private int num;
    private String nom;
    private String type;

    public SerieEntity() {}

    public SerieEntity(int num, String nom, String type) {
        this.num = num;
        this.nom = nom;
        this.type = type;
    }

    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
