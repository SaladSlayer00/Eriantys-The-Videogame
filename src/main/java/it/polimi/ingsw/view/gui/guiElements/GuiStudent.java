package it.polimi.ingsw.view.gui.guiElements;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.enums.Color;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GuiStudent extends ImageView{
    private Student student;
    public GuiStudent(Student student){
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}
