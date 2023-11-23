package it.poste.patrimonio.itf.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import lombok.Data;

/**
 * This class has the porpuse of be a business model.
 */
@Data
@XmlType(name = "Position", propOrder = {"ndg", "assets", "balance"})
public final class PositionApi implements Serializable {

    private static final long serialVersionUID = 6094641399168955491L;

	/** The id.*/
    /*@JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;*/
    
    @NotNull
    private String ndg; 
    private List<AssetApi> assets;
    
    @NotNull
    private BigDecimal balance;

	@XmlElementWrapper(name="assets") 
    @XmlElement(name="asset")
	public List<AssetApi> getAssets() {
		return assets;
	}

}
