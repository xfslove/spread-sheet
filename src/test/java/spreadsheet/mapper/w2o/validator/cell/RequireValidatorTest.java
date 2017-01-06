package spreadsheet.mapper.w2o.validator.cell;

import org.testng.annotations.Test;
import spreadsheet.mapper.TestFactory;
import spreadsheet.mapper.model.core.Cell;
import spreadsheet.mapper.model.meta.FieldMeta;

import java.util.Map;

import static org.testng.Assert.*;

/**
 * Created by hanwen on 2017/1/4.
 */
public class RequireValidatorTest {
  
  @Test
  public void testValid() throws Exception {

    Map<String, FieldMeta> fieldMetaMap = TestFactory.createFieldMetaMap();

    RequireValidator validator1 = new RequireValidator("test.int1", "");
    RequireValidator validator2 = new RequireValidator("test.int2", "");
    RequireValidator validator3 = new RequireValidator("test.long1", "");
    RequireValidator validator4 = new RequireValidator("test.long2", "");
    RequireValidator validator5 = new RequireValidator("test.float1", "");
    RequireValidator validator6 = new RequireValidator("test.float2", "");
    RequireValidator validator7 = new RequireValidator("test.double1", "");
    RequireValidator validator8 = new RequireValidator("test.double2", "");

    Map<String, Cell> cellMap1 = TestFactory.createCellMap1();
    assertTrue(validator1.valid(cellMap1.get("test.int1"), fieldMetaMap.get("test.int1")));
    assertTrue(validator2.valid(cellMap1.get("test.int2"), fieldMetaMap.get("test.int2")));
    assertTrue(validator3.valid(cellMap1.get("test.long1"), fieldMetaMap.get("test.long1")));
    assertTrue(validator4.valid(cellMap1.get("test.long2"), fieldMetaMap.get("test.long2")));
    assertTrue(validator5.valid(cellMap1.get("test.float1"), fieldMetaMap.get("test.float1")));
    assertTrue(validator6.valid(cellMap1.get("test.float2"), fieldMetaMap.get("test.float2")));
    assertTrue(validator7.valid(cellMap1.get("test.double1"), fieldMetaMap.get("test.double1")));
    assertTrue(validator8.valid(cellMap1.get("test.double2"), fieldMetaMap.get("test.double2")));

    Map<String, Cell> cellMap2 = TestFactory.createErrorCellMap();
    assertTrue(validator1.valid(cellMap2.get("test.int1"), fieldMetaMap.get("test.int1")));
    assertFalse(validator2.valid(cellMap2.get("test.int2"), fieldMetaMap.get("test.int2")));
    assertTrue(validator3.valid(cellMap2.get("test.long1"), fieldMetaMap.get("test.long1")));
    assertFalse(validator4.valid(cellMap2.get("test.long2"), fieldMetaMap.get("test.long2")));
    assertTrue(validator5.valid(cellMap2.get("test.float1"), fieldMetaMap.get("test.float1")));
    assertFalse(validator6.valid(cellMap2.get("test.float2"), fieldMetaMap.get("test.float2")));
    assertTrue(validator7.valid(cellMap2.get("test.double1"), fieldMetaMap.get("test.double1")));
    assertFalse(validator8.valid(cellMap2.get("test.double2"), fieldMetaMap.get("test.double2")));
  }

}