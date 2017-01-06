package spreadsheet.mapper.o2w.composer;

import spreadsheet.mapper.model.core.Workbook;

/**
 * workbook composer, simply adapter of sheet composer
 * <p>
 * Created by hanwen on 2017/1/4.
 */
public interface WorkbookComposeHelper {

  /**
   * @param sheetComposeHelpers sheet composer
   * @see SheetComposeHelper
   */
  WorkbookComposeHelper sheetComposer(SheetComposeHelper... sheetComposeHelpers);

  /**
   * @return composed workbook
   * @see SheetComposeHelper#compose()
   */
  Workbook compose();
}
