package com.theincgi.gles_game_fixed.utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;


/*Utility copied from eclipse workspace*/
public class ModelOptimizer {
	public static boolean DOUBLE_PERCISION = false;
	
	public static void main(String[] args) throws IOException {
		File folder = new File("C:\\Users\\TheIncgi\\AndroidStudioProjects\\GLES_GAME_fixed\\app\\src\\main\\assets");
		for(File f : folder.listFiles()) {
			if(f.toString().endsWith(".obj")) {
				File out = new File( folder, f.getName().substring(0, f.getName().length()-4) + ".mdl");
				System.out.println("Making .MDL for '"+f.getName()+"'");
				optamize(f, out);
			}
		}
		
		System.out.println("Done");
	}
	

	public static void optamize(File in, File out) throws IOException {
		ArrayList<String> lines = new ArrayList<>();
		Scanner s = new Scanner(in);
		while(s.hasNextLine()) {
			String line = s.nextLine();
			if(!line.isEmpty()) {
				line = line.trim();
				if(!line.startsWith("#")) {
					lines.add(line);
				}
			}
		}
		System.out.println("Source file had "+lines.size()+" lines");
		RandomAccessFile rafOut = new RandomAccessFile(out, "rw");
		ArrayList<Chunk> chunks = chunk( lines ); //group lines by action
		for (Chunk chunk : chunks) {
			chunk.appendChunkToFile(rafOut);
		}
		rafOut.close();

	}

	private static ArrayList<Chunk> chunk( ArrayList<String> lines ){
		ArrayList<Chunk> output = new ArrayList<>();
		String command = null;
		String data = null;
		Chunk current = null;

		for(String s : lines) {
			int index = s.indexOf(" ");
			command = s.substring(0,index);
			data = s.substring(index+1);
			CType tCType = CType.from(command);
			if(current==null || !tCType.equals(current.cType)) {
				current = new Chunk();
				current.cType = tCType;
				output.add(current);
			}
			current.lines.add(data);
			
		}

		return output;
	}

	private static class Chunk {
		CType cType;
		ArrayList<String> lines = new ArrayList<>();

		public void appendChunkToFile(RandomAccessFile raf) throws IOException {
			long chunkStart = raf.getFilePointer();

			raf.writeUTF(cType.name());

			switch (cType) {
			case MATERIAL_LIB: //MATERIAL_LIB used
			case OBJ_DEF:  //obj name
			case USE_MAT:  //MATERIAL_LIB used
			case SMOOTH_SHADING:
				break; //all only contain 1 point of info

			default:
				raf.writeInt(lines.size()); //number of elements listed in this chunk
				System.out.println(cType.name()+" x "+lines.size());
			}

			if(cType.equals(CType.FACE)) {
				ArrayList<int[]> tmp = new ArrayList<>();
				for(String line : lines) {
					Scanner scan = new Scanner(line);
					for(int i = 0; i<3; i++)
						tmp.add(splitVertex(scan.next()));
					scan.close();
				}
				for(int segment = 0; segment<3; segment++) { //vert norm or tex
					System.out.println(" >> "+(segment==0? "Vertex Index" : segment==1? "Texture Index" : "Normal Index"));
					for(int v = 0; v<tmp.size(); v++) {
						int[] f = tmp.get(v);
						System.out.println(" > "+f[segment]);
						raf.writeInt( f[segment] );
					}
				}
			}else {

				for(String line : lines) {
					switch(cType) {
					case FACE:{
						throw new RuntimeException("Should have been handled outside this loop (face)");
					}
					case MATERIAL_LIB:
						raf.writeUTF(line);
						return;
					case OBJ_DEF:
						raf.writeUTF(line);
						return;
					case USE_MAT:
						raf.writeUTF(line);
						return;
					case VERTEX:
					case NORMAL:
					{
						int s1, s2;
						s1 = line.indexOf(' ');
						s2 = line.indexOf(' ', s1+1);
						String a,b,c;
						a = line.substring(0,s1);
						b = line.substring(s1+1, s2);
						c = line.substring(s2+1);
						System.out.println(" > "+a+" "+b+" "+c);
						writeNum(raf, a);
						writeNum(raf, b);
						writeNum(raf, c);
						break;
					}
					case UV_COORD:{
						int s1 = line.indexOf(' ');
						String a, b;
						a = line.substring(0, s1);
						b = line.substring(s1+1);
						System.out.println(" > "+a+" "+b);
						writeNum( raf, a );
						writeNum( raf, b );
						break;
					}
					case SMOOTH_SHADING:
						boolean shading;
						raf.writeBoolean(shading = line.toLowerCase().equals("on"));
						System.out.println("Smooth shading: " + String.valueOf(shading));
						break;
					default:
						throw new RuntimeException("Unahandled case for " +cType.name());


					}
				}
			}
		}


		private Object select(int r, String...strings) {
			return strings[r];
		}

		private int count(String line, char c) {
			int out = 0;
			for(int i = 0; i<line.length(); i++)
				out += line.charAt(i)==c?1:0;
			return out;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			//StringJoiner j = new StringJoiner("\n\t") ;
			for (String string : lines) {
				builder.append(string);
				builder.append("\n\t");
			}
			return cType.name() + ": \n\t" +builder.toString();
		}
	}

	static String pat3 = "(\\d+)/(\\d+)?/(\\d+)";
	static String pat2 = "(\\d+)/(\\d+)";
	static String pat1 = "(\\d+)";
	static Pattern p3 = Pattern.compile(pat3);
	static Pattern p2 = Pattern.compile(pat2);
	static Pattern p1 = Pattern.compile(pat1);

	public static int[] splitVertex(String s) {
		Matcher m;
		int[] out;
		if(p3.matcher(s).matches()) {
			out = new int[3];
			m = p3.matcher(s);
		}else if( p2.matcher(s).matches()) {
			out = new int[2];
			m = p2.matcher(s);
		}else if( p1.matcher(s).matches() ) {
			out = new int[1];
			m = p1.matcher(s);
		}else {
			throw new RuntimeException("Invalid format!"); 
		}
		m.find();
		for(int i = 0; i<out.length; i++) {
			String text = m.group(i+1);
			text= text==null?"":text;
			out[i] = Integer.parseInt(text.isEmpty()?"0":text)-1;
		}
		return out;
	}

	public static void writeNum(RandomAccessFile raf, String num) throws NumberFormatException, IOException {
		if(DOUBLE_PERCISION) {
			raf.writeDouble( parseDouble(num) );
		}else {
			raf.writeFloat( parseFloat(num) );
		}
	}

	private enum CType{
		MATERIAL_LIB,
		VERTEX,
		OBJ_DEF,
		NORMAL,
		FACE,
		USE_MAT,
		SMOOTH_SHADING,
		UV_COORD;

		public static CType from(String s) {
			switch (s) {
			case "mtllib":
				return MATERIAL_LIB;
			case "v":
				return VERTEX;
			case "vn":
				return NORMAL;
			case "o":
				return OBJ_DEF;
			case "f":
				return FACE;
			case "s":
				return SMOOTH_SHADING;
			case "usemtl":
				return USE_MAT;
			case "vt":
				return UV_COORD;
			default:
				throw new RuntimeException("Whats \""+s+"\"?");
			}
		}
	}
}
