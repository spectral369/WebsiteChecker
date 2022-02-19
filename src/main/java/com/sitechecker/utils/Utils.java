package com.sitechecker.utils;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utils {

    static String basepath = Utils.class.getClassLoader().getResource("").getPath();

    public static boolean isNetAvailable1() {
	try {

	    final URL url = new URL("http://www.google.com");
	    final URLConnection conn = url.openConnection();
	    conn.setConnectTimeout(8000);
	    conn.connect();
	    return true;
	} catch (MalformedURLException e) {
	    throw new RuntimeException(e);
	} catch (IOException e) {
	    return false;
	}
    }

    @SuppressWarnings("unused")
    private static boolean isNetAvailable2(final URL url, final int timeout) {
	URLConnection conn;
	try {
	    conn = url.openConnection();

	    conn.setConnectTimeout(timeout);
	    conn.setReadTimeout(timeout);
	    InputStream in = conn.getInputStream();
	    in.close();
	} catch (IOException e) {

	    e.printStackTrace();
	    return false;
	}

	return true;

    }

    public static int checkSite(String protocol, String Site) {
	HttpURLConnection connection = null;
	int code = -1;
	try {
	    String s = protocol + Site;
	    URL u = new URL(s);
	    connection = (HttpURLConnection) u.openConnection();
	    connection.setConnectTimeout(3000);
	    connection.setRequestMethod("HEAD");
	    HttpURLConnection.setFollowRedirects(true);
	    connection.addRequestProperty("User-Agent",
		    "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:97.0) Gecko/20100101 Firefox/97.0");

	    connection.addRequestProperty("Connection", "keep-alive");
	    connection.addRequestProperty("Pragma", "no-cache");

	    connection.connect();
	    code = connection.getResponseCode();
	    if (code < 100) {
		System.out.println("reachable, not operating");
	    }
	    return code;

	} catch (IOException e) {

	    e.printStackTrace();
	    return code;
	} finally {
	    if (connection != null) {
		connection.disconnect();
	    }

	}

    }

    public static String codeMeaning(int code) {
	String s = "";
	switch (code) {
	case 200:
	    s = "200 =  OK \n Meaning site works as it should";
	    break;
	case 404:
	    s = "404 =  Page/site Not found \n Meaning the link you've accessed is not available";
	    break;
	default:
	    s = "mi-a fost lene sa completez lista...\n Un elefant se legana pe o panza...";
	    break;

	}
	return s;
    }

    public static void getThumbnail(String protocol2, String Site) {

	String os = System.getProperty("os.name").toUpperCase();

	if (os.contains("LINUX")) {

	    InputStream res = Utils.class.getClassLoader().getResourceAsStream("META-INF/resources/chromedriver");
	    File target = new File(
		    System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + "chromedriver");
	    if (!target.exists())
		try {

		    Files.copy(res, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		    Set<PosixFilePermission> perms = new HashSet<>();
		    perms.add(PosixFilePermission.OWNER_READ);
		    perms.add(PosixFilePermission.OWNER_WRITE);
		    perms.add(PosixFilePermission.OWNER_EXECUTE);

		    Files.setPosixFilePermissions(target.toPath(), perms);
		} catch (IOException e1) {

		    System.out.println("Bigerror");
		    e1.printStackTrace();

		}

	    if (!target.canExecute())
		try {
		    throw new FileNotFoundException("chrome(linux) copy did not work!");
		} catch (FileNotFoundException e1) {

		    e1.printStackTrace();
		}
	    try {
		System.setProperty("webdriver.chrome.driver", target.getCanonicalPath());
	    } catch (IOException e1) {

		e1.printStackTrace();
	    }
	} else if (os.contains("WIN")) {
	    InputStream res = Utils.class.getClassLoader().getResourceAsStream("META-INF/resources/chromedriver");
	    File target = new File(System.getProperty("java.io.tmpdir") + "chromedriver.exe");
	    if (!target.exists())
		try {

		    Files.copy(res, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e1) {

		    e1.printStackTrace();
		}
	    if (!target.canExecute())
		try {
		    throw new FileNotFoundException("chrome.exe(win) copy did not work!");
		} catch (FileNotFoundException e1) {

		    e1.printStackTrace();
		}
	    try {
		System.setProperty("webdriver.chrome.driver", target.getCanonicalPath());
	    } catch (IOException e1) {

		e1.printStackTrace();

	    }

	}

	ChromeOptions options = new ChromeOptions();
	options.addArguments("enable-automation");
	options.addArguments("--disable-infobars");
	options.addArguments("--start-maximized");
	options.addArguments("--start-fullscreen");
	options.addArguments("--mute-audio");
	options.addArguments("window-size=1366x768");
	options.addArguments("--no-sandbox");
	options.addArguments("--disable-dev-shm-usage");
	options.addArguments("--no-sandbox");
	options.addArguments("--remote-debugging-port=9222");
	options.addArguments("--headless");

	WebDriver driver = new ChromeDriver(options);
	WebDriverWait wait = new WebDriverWait(driver, 10);
	String s = usesHttps(Site) + Site;
	driver.navigate().to(s);
	wait.until(new ExpectedCondition<Boolean>() {

	    @Override
	    public Boolean apply(WebDriver arg0) {

		return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
	    }

	});
	File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

	try {

	    Files.copy(scrFile.toPath(), new File(getThumbnailFolder() + getSitenameProcessed(Site) + ".png").toPath(),
		    StandardCopyOption.REPLACE_EXISTING);

	} catch (IOException e) {

	    e.printStackTrace();
	    driver.quit();
	}

	driver.quit();
    }

    @SuppressWarnings("unused")
    private static BufferedImage scale(BufferedImage source, double ratio) {
	int w = (int) (source.getWidth() * ratio);
	int h = (int) (source.getHeight() * ratio);
	BufferedImage bi = getCompatibleImage(w, h);
	Graphics2D g2d = bi.createGraphics();
	double xScale = (double) w / source.getWidth();
	double yScale = (double) h / source.getHeight();
	AffineTransform at = AffineTransform.getScaleInstance(xScale, yScale);
	g2d.drawRenderedImage(source, at);
	g2d.dispose();
	return bi;
    }

    private static BufferedImage getCompatibleImage(int w, int h) {
	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	GraphicsDevice gd = ge.getDefaultScreenDevice();
	GraphicsConfiguration gc = gd.getDefaultConfiguration();
	BufferedImage image = gc.createCompatibleImage(w, h);
	return image;
    }

    public static String getThumbnailFolder() {

	File fol = new File(basepath + "META-INF/resources/images/");
	if (fol.exists())
	    return basepath + "META-INF/resources/images/";
	else
	    return basepath + "META-INF/resources/images/";

    }

    public static String getSitenameProcessed(String site) {

	StringTokenizer st = new StringTokenizer(site, ".");
	List<String> arr = new LinkedList<>();
	while (st.hasMoreTokens())
	    arr.add(st.nextToken());
	String process = arr.get(arr.size() - 1) + "_" + arr.get(arr.size() - 2);

	return process;
    }

    public static String pathToImg(String site) {

	File img = new File(Utils.getThumbnailFolder() + Utils.getSitenameProcessed(site) + ".png");
	if (img.exists())
	    return "META-INF/resources/images/" + Utils.getSitenameProcessed(site) + ".png";
	else {

	    return "META-INF/resources/images/noimg.png";
	}

    }

    public static boolean parseURL(String protocol, String siteName) {

	String[] schemes = { "http", "https" };
	UrlValidator valid = new UrlValidator(schemes);
	String url = protocol + "://" + siteName;
	if (valid.isValid(url)) {
	    return true;
	} else
	    return false;

    }

    public static boolean parseURL(StringBuilder siteName) {

	if (!isWWWPresent(siteName.toString()))
	    siteName.insert(0, "www.");

	if (!isValidURL(siteName.toString()))
	    return false;

	String protocol = usesHttps(siteName.toString());

	if (protocol != "error") {
	    String[] schemes = { "http", "https" };
	    UrlValidator valid = new UrlValidator(schemes);
	    String url = protocol + siteName;
	    if (valid.isValid(url)) {

		return true;
	    } else
		return false;
	} else
	    return false;

    }

    public static boolean isWWWPresent(String siteName) {

	return siteName.matches("(?i)^.*www.*$");

    }

    public static String usesHttps(final String urlWithoutProtocol) {

	try {
	    Jsoup.connect("https://" + urlWithoutProtocol).get();

	    return "https://";
	} catch (final IOException e) {
	    try {
		Jsoup.connect("http://" + urlWithoutProtocol).get();
	    } catch (IOException e1) {

		System.out.println(e.getLocalizedMessage());
		return "error";
	    }
	    return "http://";
	}
    }

    public static boolean isValidURL(String Site) {

	boolean s = Site.matches(".*\\w+{1,}\\b\\.\\w{2,3}\\b");
	return s;
    }

    public static boolean isSetupFilePresent() {

	File f = new File(basepath + "//configuration.ini");
	return f.getAbsoluteFile().exists();
    }

    public static File getSetupFile() {

	return new File(basepath + "//configuration.ini");
    }

    public static String simpleEnc(String plain) {
	String b64encoded = Base64.getEncoder().encodeToString(plain.getBytes());
	String reverse = new StringBuffer(b64encoded).reverse().toString();

	StringBuilder tmp = new StringBuilder();
	final int OFFSET = 4;
	for (int i = 0; i < reverse.length(); i++) {
	    tmp.append((char) (reverse.charAt(i) + OFFSET));
	}
	return tmp.toString();
    }

    public static String simpleDec(String secret) {
	StringBuilder tmp = new StringBuilder();
	final int OFFSET = 4;
	for (int i = 0; i < secret.length(); i++) {
	    tmp.append((char) (secret.charAt(i) - OFFSET));
	}

	String reversed = new StringBuffer(tmp.toString()).reverse().toString();
	return new String(Base64.getDecoder().decode(reversed));
    }

}
