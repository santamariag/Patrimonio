package it.poste.patrimonio.db.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * This class has the porpuse of be a business model.
 */
@Data
@Entity
@Table(name = "positions")
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Position implements Serializable {

    private static final long serialVersionUID = 6094641399168955491L;

	/** The id.*/
    /*@JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;*/
    
    @Id
    private String ndg; 
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Asset> assets=new ArrayList<Asset>();
    
    @NotNull
    private Double balance;


}
