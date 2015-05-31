package ic2.core.init;

import java.io.File;
import java.io.FilenameFilter;

final class Localization$1 implements FilenameFilter
{
    public boolean accept(File dir, String name)
    {
        return name.endsWith(".properties");
    }
}
