package ic2.core.init;

import com.google.common.base.Charsets;
import cpw.mods.fml.common.registry.LanguageRegistry;
import ic2.core.IC2;
import ic2.core.init.Localization$1;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Localization
{
    public static void init(File modSourceFile)
    {
        if (modSourceFile.isDirectory())
        {
            File zipFile = new File(modSourceFile, "ic2/lang");

            if (zipFile.isDirectory())
            {
                File[] e = zipFile.listFiles(new Localization$1());
                int entry = e.length;

                for (int name = 0; name < entry; ++name)
                {
                    File langFile = e[name];

                    try
                    {
                        loadLocalization(langFile.toURI().toURL().openStream(), langFile.getName().split("\\.")[0]);
                    }
                    catch (Exception var16)
                    {
                        var16.printStackTrace();
                        IC2.log.warning("can\'t read language file");
                    }
                }
            }
            else
            {
                IC2.log.warning("can\'t list language files (from folder)");
            }
        }
        else if (modSourceFile.exists() && modSourceFile.getName().endsWith(".jar"))
        {
            ZipFile var19 = null;

            try
            {
                var19 = new ZipFile(modSourceFile);
                Enumeration var20 = var19.entries();

                while (var20.hasMoreElements())
                {
                    ZipEntry var21 = (ZipEntry)var20.nextElement();
                    String var22 = var21.getName();

                    if (var22.startsWith("ic2/lang/"))
                    {
                        var22 = var22.substring("ic2/lang/".length());

                        if (!var22.contains("/") && var22.endsWith(".properties"))
                        {
                            loadLocalization(var19.getInputStream(var21), var22.split("\\.")[0]);
                        }
                    }
                }
            }
            catch (Exception var17)
            {
                var17.printStackTrace();
                IC2.log.warning("can\'t list language files (from jar)");
            }
            finally
            {
                if (var19 != null)
                {
                    try
                    {
                        var19.close();
                    }
                    catch (IOException var15)
                    {
                        ;
                    }
                }
            }
        }
        else
        {
            IC2.log.warning("can\'t find language files");
        }
    }

    public static void loadLocalization(InputStream inputStream, String lang) throws IOException
    {
        Properties properties = new Properties();
        properties.load(new InputStreamReader(inputStream, Charsets.UTF_8));
        Iterator i$ = properties.entrySet().iterator();

        while (i$.hasNext())
        {
            Entry entries = (Entry)i$.next();
            Object key = entries.getKey();
            Object value = entries.getValue();

            if (key instanceof String && value instanceof String)
            {
                String newKey = (String)key;

                if (!newKey.startsWith("achievement.") && !newKey.startsWith("itemGroup.") && !newKey.startsWith("death."))
                {
                    newKey = "ic2." + newKey;
                }

                LanguageRegistry.instance().addStringLocalization(newKey, lang, (String)value);
            }
        }
    }

    public static void addLocalization(String name, String desc)
    {
        LanguageRegistry.instance().addStringLocalization(name, desc);
    }
}
