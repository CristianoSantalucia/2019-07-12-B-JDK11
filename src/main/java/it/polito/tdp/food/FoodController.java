/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.Portion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController
{
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtPorzioni"
	private TextField txtPorzioni; // Value injected by FXMLLoader

	@FXML // fx:id="txtK"
	private TextField txtK; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalisi"
	private Button btnAnalisi; // Value injected by FXMLLoader

	@FXML // fx:id="btnGrassi"
	private Button btnGrassi; // Value injected by FXMLLoader

	@FXML // fx:id="btnSimula"
	private Button btnSimula; // Value injected by FXMLLoader

	@FXML // fx:id="boxFood"
	private ComboBox<Food> boxFood; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML void doCreaGrafo(ActionEvent event)
	{
		txtResult.clear();
		
		Integer num = null; 
		try
		{
			this.txtResult.appendText("\nCreazione grafo...");
			
			num = Integer.parseInt(this.txtPorzioni.getText()); 
			this.model.creaGrafo(num);
			this.boxFood.getItems().addAll(this.model.getVertex());
			
			this.txtResult.appendText("\n#Vertici: " + this.model.getNumVertici() + "\n#Archi: " + this.model.getNumArchi());
		}
		catch (Exception e)
		{
			this.txtResult.setText("ERRORE");
			e.printStackTrace();
		}
	}

	@FXML void doGrassi(ActionEvent event)
	{
		txtResult.clear();
		txtResult.appendText("Analisi grassi...");
		try
		{
			Food f = this.boxFood.getValue();
			txtResult.appendText("" + this.model.getSuccessiviMin(f));
		}
		catch (Exception e)
		{
			this.txtResult.setText("ERRORE");
			e.printStackTrace();
		}
	}

	@FXML void doSimula(ActionEvent event)
	{
		txtResult.clear();
		txtResult.appendText("Simulazione...");
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize()
	{
		assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
		assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnGrassi != null : "fx:id=\"btnGrassi\" was not injected: check your FXML file 'Food.fxml'.";
		assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
		assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
	}

	public void setModel(Model model)
	{
		this.model = model;
	}
}
