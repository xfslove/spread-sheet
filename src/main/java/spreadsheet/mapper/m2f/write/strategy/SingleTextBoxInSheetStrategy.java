package spreadsheet.mapper.m2f.write.strategy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import spreadsheet.mapper.model.msg.Message;
import spreadsheet.mapper.model.msg.MessageWriteStrategies;
import spreadsheet.mapper.model.shapes.TextBox;
import spreadsheet.mapper.model.shapes.TextBoxBean;
import spreadsheet.mapper.model.shapes.TextBoxStyle;

import java.util.*;

/**
 * use text box to write messages strategy and one sheet one text box
 * <p>
 * Created by hanwen on 2017/1/3.
 */
public class SingleTextBoxInSheetStrategy implements MessageWriteStrategy {

  private static final String ENTER_SEPARATOR = "\n";

  @Override
  public String getStrategy() {
    return MessageWriteStrategies.TEXT_BOX;
  }

  @Override
  public void write(Workbook workbook, Collection<Message> messages) {

    if (CollectionUtils.isEmpty(messages)) {
      return;
    }

    int numberOfSheets = workbook.getNumberOfSheets();

    // remove old text boxes
    for (int i = 0; i < numberOfSheets; i++) {

      Sheet sheet = workbook.getSheetAt(i);
      if (sheet instanceof XSSFSheet) {
        removeXSSFTextBox((XSSFSheet) sheet);
      } else {
        removeHSSFTextBox((HSSFSheet) sheet);
      }
    }

    // create new text boxes
    List<TextBox> textBoxes = transferToTextBoxes(messages);

    for (TextBox textBox : textBoxes) {


      while (numberOfSheets < textBox.getSheetIndex()) {
        workbook.createSheet();
        numberOfSheets = workbook.getNumberOfSheets();
      }

      Sheet sheet = workbook.getSheetAt(textBox.getSheetIndex() - 1);
      if (sheet instanceof XSSFSheet) {
        addXSSFTextBox((XSSFSheet) sheet, textBox);
      } else {
        addHSSFTextBox((HSSFSheet) sheet, textBox);
      }
    }
  }

  private List<TextBox> transferToTextBoxes(Collection<Message> messages) {

    Map<Integer, List<String>> textBoxMessageMap = new HashMap<>();
    for (Message message : messages) {

      int sheetIndex = message.getSheetIndex();

      if (!textBoxMessageMap.containsKey(sheetIndex)) {
        textBoxMessageMap.put(sheetIndex, new ArrayList<String>());
      }
      textBoxMessageMap.get(sheetIndex).add(message.getMessage());
    }

    List<TextBox> textBoxes = new ArrayList<>();
    for (Map.Entry<Integer, List<String>> entry : textBoxMessageMap.entrySet()) {
      textBoxes.add(new TextBoxBean(StringUtils.join(entry.getValue(), ENTER_SEPARATOR), entry.getKey()));
    }

    return textBoxes;
  }

  private void removeXSSFTextBox(XSSFSheet xssfSheet) {
    XSSFDrawing drawingPatriarch = xssfSheet.createDrawingPatriarch();

    // current remove all the text boxes in sheet rudely
    List<XSSFShape> textboxes = new ArrayList<>();
    for (XSSFShape shape : drawingPatriarch.getShapes()) {
      if (shape instanceof XSSFTextBox) {
        textboxes.add(shape);
      }
    }

    drawingPatriarch.getShapes().removeAll(textboxes);
  }

  private void addXSSFTextBox(XSSFSheet xssfSheet, TextBox textBox) {
    TextBoxStyle style = textBox.getStyle();

    XSSFDrawing drawingPatriarch = xssfSheet.createDrawingPatriarch();
    XSSFClientAnchor anchor = drawingPatriarch.createAnchor(0, 0, 0, 0, style.getCol1() - 1, style.getRow1() - 1, style.getCol2() - 1, style.getRow2() - 1);

    XSSFTextBox textbox = drawingPatriarch.createTextbox(anchor);
    textbox.setText(new XSSFRichTextString(textBox.getMessage()));
    textbox.setFillColor(style.getRed(), style.getGreen(), style.getBlue());

  }

  private void removeHSSFTextBox(HSSFSheet hssfSheet) {
    HSSFPatriarch drawingPatriarch = hssfSheet.createDrawingPatriarch();

    // current remove all the text boxes in sheet rudely
    List<HSSFShape> textboxes = new ArrayList<>();
    for (HSSFShape shape : drawingPatriarch.getChildren()) {
      if (shape instanceof HSSFTextbox) {
        textboxes.add(shape);
      }
    }

    drawingPatriarch.getChildren().removeAll(textboxes);
  }

  private void addHSSFTextBox(HSSFSheet hssfSheet, TextBox textBox) {
    TextBoxStyle style = textBox.getStyle();

    HSSFPatriarch drawingPatriarch = hssfSheet.createDrawingPatriarch();
    HSSFClientAnchor anchor = drawingPatriarch.createAnchor(0, 0, 0, 0, style.getCol1() - 1, style.getRow1() - 1, style.getCol2() - 1, style.getRow2() - 1);

    HSSFTextbox textbox = drawingPatriarch.createTextbox(anchor);
    textbox.setString(new HSSFRichTextString(textBox.getMessage()));
    textbox.setFillColor(style.getRed(), style.getGreen(), style.getBlue());

  }
}
