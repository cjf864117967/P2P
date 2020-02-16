package cn.itcast.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * 文件处理工具类
 * <pre>
 * 1.文件转移copy方法
 * </pre>
 */
public abstract class FileUtil {
	/** 默认文件缓存大小 **/
	public final static int FILE_BUFFER_SIZE = 32 * 1024;

	/**
	 * 将指定文件的内容复制到指定文件中
	 * 
	 * @param fileInpath
	 *            读取的文件路径完整路径(包括文件名称)串
	 * @param fileOutpath
	 *            写入目标文件完整路径(包括文件名称)串
	 * @throws IOException
	 */
	public static void copy(String fileInpath, String fileOutpath)
			throws IOException {
		copy(new File(fileInpath), new File(fileOutpath), FILE_BUFFER_SIZE);
	}

	/**
	 * 将指定文件的内容复制到指定文件中(可以指定缓存大小)
	 * 
	 * @param fileInpath
	 *            读取的文件路径完整路径(包括文件名称)串
	 * @param fileOutpath
	 *            写入目标文件完整路径(包括文件名称)串
	 * @param bufsize
	 *            缓存大小
	 * @throws IOException
	 */
	public static void copy(String fileInpath, String fileOutpath, int bufsize)
			throws IOException {
		copy(new File(fileInpath), new File(fileOutpath), bufsize);
	}
	/**
	 * 将指定文件的内容复制到指定文件中
	 * 
	 * @param fileIn
	 *            读取的文件
	 * @param fileOut
	 *            写入的目标文件
	 * @throws IOException
	 */
	public static void copy(File fileIn, File fileOut) throws IOException {
		copy(fileIn, fileOut, FILE_BUFFER_SIZE);
	}
	/**
	 * 将指定文件的内容复制到指定文件中(可以指定缓存大小)
	 * 
	 * @param fileIn
	 *            读取的文件
	 * @param fileOut
	 *            写入的目标文件
	 * @param bufsize
	 *            缓存大小
	 * @throws IOException
	 */
	public static void copy(File fileIn, File fileOut, int bufsize)
			throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(fileIn);
			out = new FileOutputStream(fileOut);
			byte[] buf = new byte[bufsize];
			int read = 0;
			while ((read = in.read(buf, 0, bufsize)) != -1) {
				out.write(buf, 0, read);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
		}
	}
    /** 
     *根据路径删除指定的目录或文件，无论存在与否(包括内部的子文件夹 )
     *@param filePath  要删除的目录或文件 
     *@return 删除成功返回 true，否则返回 false。 
     */  
    public static boolean  DeleteFolder(String filePath) {  
       boolean flag = false;  
       File file = new File(filePath);
        if (!file.exists()) {   
            return flag;  
        } else {  
            if (file.isFile()) {  
                return deleteFile(filePath);  
            } else {  
                return deleteDirectory(filePath);  
            }  
        }  
    }  
    /** 
     * 删除单个文件 
     * @param   filePathName  被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public static boolean  deleteFile(String filePathName) {  
    	boolean  flag = false;  
    	File  file = new File(filePathName);  
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
            flag = true;  
        }  
        return flag;  
    }
    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param   fileDir 被删除目录的文件路径 
     * @return  目录删除成功返回true，否则返回false 
     */  
    public static boolean  deleteDirectory(String fileDir) {  
        if (!fileDir.endsWith(File.separator)) {  
        	fileDir = fileDir + File.separator;  
        }  
        File dirFile = new File(fileDir);  
        if (!dirFile.exists() || !dirFile.isDirectory()) {  
            return false;  
        }  
        boolean flag = true;  
        File[] files = dirFile.listFiles();  
        for (int i = 0; i < files.length; i++) {  
            if (files[i].isFile()) {  
                flag = deleteFile(files[i].getAbsolutePath());  
                if (!flag) break;  
            }
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag) break;  
            }  
        }  
        if (!flag) return false;  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    } 
    /**
     * 将指定文件流复制到指定的文件中
     * @param fileIn
     * @param fileOut
     */
    public static void copyInputStream(InputStream fileIn, File fileOut){
    	InputStream in = fileIn;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(fileOut);
			byte[] buf = new byte[FILE_BUFFER_SIZE];
			int read = 0;
			while ((read = in.read(buf, 0, FILE_BUFFER_SIZE)) != -1) {
				out.write(buf, 0, read);
			}
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }
    /**
     * 在指定文件夹下建立子文件夹
     * @param parentDir
     * @param suBdir
     * <pre>
     * String pdir="/upfiles"
     * String sdir="crash";
     * FileUtil.makeDir(pdir,sdir);
     * </pre>
     * @throws Exception
     */
    public static boolean makeDir(String parentDir,String suBdir) throws Exception{
    	File pdir = new File(parentDir);
    	System.out.println("pdir=============" + pdir);
    	if(!pdir.isDirectory()){
    		throw new RuntimeException("parentDir is directory exits");
    	}
    	if(!pdir.exists()){
    		throw new RuntimeException("parentDir is not exits");
    	}else{
    		File sdir = new File(parentDir+File.separator+suBdir);
    		if(!sdir.exists()){
    			sdir.mkdir();
    			return true;
    		}
    		return true;
    	}
    }
}
