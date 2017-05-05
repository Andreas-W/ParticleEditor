package parser;


import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

import util.Util;
import entitytypes.FXListType;
import entitytypes.FXListType.ParticleSystemEntry;
import entitytypes.FXListType.ParticleSystemEntry.RandomFloatEntry;
import entitytypes.FXListType.ParticleSystemEntry.RandomIntEntry;
import entitytypes.FXListType.e_RandomType;
import entitytypes.ParticleSystemType;
import entitytypes.ParticleSystemType.posEntry;

public class Parser {
	
	public static ArrayList<String> errors = new ArrayList<String>();
	
	public static void parseParticleSystemINI(String fileName, HashMap<String, ParticleSystemType> particleSystemTypes) throws FileNotFoundException {
		StringBuffer log = new StringBuffer();
		File file = new File(fileName);
		Scanner sc = new Scanner(file);
		sc.useLocale(Locale.ENGLISH);
		sc.useDelimiter("\\s+;.*\\s+|;.*\\s+|(\\s*=)?\\s*[rRgGbBxXyYzZ]\\s*:\\s*|(\\s*=\\s*)|\\s+");
		String next = "";
		//StringBuilder error = new StringBuilder();
		boolean eof = false;
		while (!eof && sc.hasNextLine() || sc.hasNext()) {
			if (sc.hasNext()) next = sc.next();
			else eof = true;
			if (next.toLowerCase().equals("particlesystem") && sc.hasNext()) {
				String name = sc.next();
				boolean end = false;
				ParticleSystemType type = new ParticleSystemType();
				while (sc.hasNextLine() && !end) {
					if (sc.hasNext()) next = sc.next();
					if (next.toLowerCase().equals("end")) {
						end = true;
					}else {
						try {
							//System.out.println(next);
							parseToken(next, type, sc);
							if (errors.size() > 0) {
								log.append("PS: '"+name+"': ");
								for (String s : errors) {
									log.append(s).append("\n");
								}
								errors.clear();
							}
						} catch (IllegalArgumentException
								| IllegalAccessException e) {
							log.append("PS: '"+name+"': could not parse token '"+ next+"'\n");
						}
					}
				}
				particleSystemTypes.put(name, type);
				//System.out.println("Added ParticleSystem "+name);
			}else if (next.toLowerCase().equals("fxlist")) {
				boolean end = false;
				while (sc.hasNextLine() && !end) {
					if (sc.hasNext()) next = sc.next();
					if (next.toLowerCase().equals("end")) {
						end = true;
					}else {
						if (next.toLowerCase().equals("particlesystem") || isAdditionalEntry(next)) {
							while (sc.hasNextLine() && !((next = sc.nextLine()).trim().toLowerCase()).equals("end")) {
								//++
							}
						}
					}
				}	
			}
		}
		
		sc.close();
		Util.printToLog(log.toString());
//		System.out.println("ParticleSystem loading finished.");
//		System.out.println(log.toString());
	}
	
	private static void parseToken(String token, Object type, Scanner sc) throws IllegalArgumentException, IllegalAccessException {
		Field field = null;
		try {
			field = type.getClass().getField(token);
			Class<?> c = field.getType();
			try {
				if (c == String.class) {
					String value = getStringValue(sc);
					if(field.getName().equals("ParticleName")) {
						field.set(type, value.toLowerCase());
					}else {
						field.set(type, value);
					}					
				}else if (c == Float.TYPE) {
					float value = getFloatValue(sc);
					field.setFloat(type, value);
				}else if (c == Integer.TYPE) {
					int value = getIntValue(sc);
					field.setInt(type, value);
				}else if (c == Boolean.TYPE) {
					boolean value = getBooleanValue(sc);
					field.setBoolean(type, value);
				}else if (c == float[].class) {
					float[] f = (float[]) field.get(type);
					int l = f.length;
					for (int i = 0; i < l; i++) {
						f[i] = getFloatValue(sc);
					}
				}else if (c == int[].class) {
					int[] f = (int[]) field.get(type);
					int l = f.length;
					for (int i = 0; i < l; i++) {
						f[i] = getIntValue(sc);
					}
				}else if (c == ParticleSystemType.posEntry.class) {
					float x = 0.0f, y = 0.0f, z = 0.0f;
					x = getFloatValue(sc);	
					y = getFloatValue(sc);					
					z = getFloatValue(sc);					
					field.set(type, ((ParticleSystemType)type).new posEntry(x, y, z));
				}else if (c == ParticleSystemType.colorEntry.class) {
					int r = 0, g = 0, b = 0, t = 0;
					r = getIntValue(sc);
					g = getIntValue(sc);
					b = getIntValue(sc);
					t = getIntValue(sc);
					field.set(type, ((ParticleSystemType)type).new colorEntry(r, g, b, t));
				}else if (c == ParticleSystemType.alphaEntry.class) {
					float a1 = 1.0f, a2 = 1.0f;
					int t = 0;
					a1 = getFloatValue(sc);
					a2 = getFloatValue(sc);
					t = getIntValue(sc);
					field.set(type,((ParticleSystemType)type).new alphaEntry(a1, a2, t));
				}else if (c.isEnum()) { //Enum types
					String value = getStringValue(sc);
					field.set(type, Enum.valueOf(field.getType().asSubclass(Enum.class), value));
				}else if ( c == FXListType.ParticleSystemEntry.RandomFloatEntry.class) {
					float f1 = getFloatValue(sc);
					float f2 = getFloatValue(sc);
					String value = getStringValue(sc);
					RandomFloatEntry entry = ((ParticleSystemEntry)type).new RandomFloatEntry(f1, f2, e_RandomType.valueOf(value));
					field.set(type, entry);
				}else if ( c == FXListType.ParticleSystemEntry.RandomIntEntry.class) {
					int i1 = getIntValue(sc);
					int i2 = getIntValue(sc);
					String value = getStringValue(sc);
					RandomIntEntry entry = ((ParticleSystemEntry)type).new RandomIntEntry(i1, i2, e_RandomType.valueOf(value));
					field.set(type, entry);
				}
			}catch (ParseErrorException e) {
				errors.add(token + ": " + e.msg);
			}
			
		} catch (NoSuchFieldException e){
			//System.out.println("Skipped token '"+token+"'.");
		}
	}
	
	//- FXLIST ---
	
	public static void parseFXListINI(String fileName, HashMap<String, FXListType> FXListTypes) throws FileNotFoundException {
		StringBuffer log = new StringBuffer();
		File file = new File(fileName);
		Scanner sc = new Scanner(file);
		sc.useLocale(Locale.ENGLISH);
		sc.useDelimiter("\\s+;.*\\s+|;.*\\s+|(\\s*=)?\\s*[rRgGbBxXyYzZ]\\s*:\\s*|(\\s*=\\s*)|\\s+");
		String next = "";
		boolean eof = false;
		//StringBuilder error = new StringBuilder();
		while (!eof && sc.hasNextLine() || sc.hasNext()) {
			if (sc.hasNext()) next = sc.next();
			else eof = true;
			if (next.toLowerCase().equals("fxlist")) {
				String name = sc.next();
				boolean end = false;
				FXListType type = new FXListType();
				while (sc.hasNextLine() && !end) {
					if (sc.hasNext()) next = sc.next();
					if (next.toLowerCase().equals("end")) {
						end = true;
					}else {
					  //Parse ParticleSystem entries
						if (next.toLowerCase().equals("particlesystem")) {
							end = false;
							FXListType.ParticleSystemEntry entry = type.new ParticleSystemEntry();
							while (sc.hasNextLine() && !end) {
								if (sc.hasNext()) next = sc.next();
								if (next.toLowerCase().equals("end")) {
									end = true;
								}else {
									try {
										parseToken(next, entry, sc);
										if (errors.size() > 0) {
											log.append("FX: '"+name+"': ");
											for (String s : errors) {
												log.append(s).append("\n");
											}
											//log.append("---\n");
											errors.clear();
										}
									} catch (IllegalArgumentException
											| IllegalAccessException e) {
										log.append("FX '"+name+"': could not parse token '"+ next+"'\n");
									}
								}
							}
							type.ParticleSystems.add(entry);
							end = false;
						}else if (isAdditionalEntry(next)) {
							type.additionalEntries += next;
							while (sc.hasNextLine() && !((next = sc.nextLine()).trim().toLowerCase()).equals("end")) {
								type.additionalEntries+= "  "+(next.trim())+"\n";
							};
							type.additionalEntries+="End\n";
						}
					}
				}
				FXListTypes.put(name, type);
				//System.out.println("Added FXList "+name);
			}

		}
		
		sc.close();
		//System.out.println("FXList loading finished.");
		Util.printToLog(log.toString());
		//System.out.println(log.toString());
	}
	//----------------
	
	private static boolean isAdditionalEntry(String next) {
		return next.toLowerCase().equals("lightpulse") || 
				next.toLowerCase().equals("sound") ||
				next.toLowerCase().equals("viewshake") ||
				next.toLowerCase().equals("tracer") ||
				next.toLowerCase().equals("terrainscorch");
	}
	
	public static FXListType parseFXListCode(String code, String name) {
		Scanner sc = new Scanner(code);
		sc.useLocale(Locale.ENGLISH);
		sc.useDelimiter("\\s+;.*\\s+|;.*\\s+|(\\s*=)?\\s*[rRgGbBxXyYzZ]\\s*:\\s*|(\\s*=\\s*)|\\s+");
		String next = "";
		StringBuilder log = new StringBuilder();
		FXListType type = new FXListType();
		boolean eof = false;
		while (sc.hasNextLine() && !eof) {
			if (sc.hasNext()) next = sc.next();
			else eof = true;
			if (next.equals("")) eof = true;
			boolean end;
		  //Parse ParticleSystem entries
			if (next.toLowerCase().equals("particlesystem")) {
				end = false;
				FXListType.ParticleSystemEntry entry = type.new ParticleSystemEntry();
				while (sc.hasNextLine() && !end) {
					if (sc.hasNext()) next = sc.next();
					if (next.toLowerCase().equals("end")) {
						end = true;
					}else {
						try {
							parseToken(next, entry, sc);
							if (errors.size() > 0) {
								log.append(name).append("\n");
								for (String s : errors) {
									log.append("  ").append(s).append("\n");
								}
								log.append("---\n");
								errors.clear();
							}
						} catch (IllegalArgumentException
								| IllegalAccessException e) {
							log.append("could not parse token '"+ next+"'\n");
						}
					}
				}
				type.ParticleSystems.add(entry);
				end = false;
			}else if (isAdditionalEntry(next)) {
				type.additionalEntries += next;
				while (sc.hasNextLine() && !((next = sc.nextLine()).trim().toLowerCase()).equals("end")) {
					type.additionalEntries+= (next)+"\n";
				};
				type.additionalEntries+="End\n";
			}
		}
		return type;
	}
	

	public static ParticleSystemType parseParticleSystemCode(String code, String name) {
		ParticleSystemType type = new ParticleSystemType();
		Scanner sc = new Scanner(code);
		sc.useLocale(Locale.ENGLISH);
		sc.useDelimiter("\\s+;.*\\s+|;.*\\s+|(\\s*=)?\\s*[rRgGbBxXyYzZ]\\s*:\\s*|(\\s*=\\s*)|\\s+");
		String next = "";
		StringBuilder log = new StringBuilder();
		boolean eof = false;
		while (sc.hasNextLine() && !eof) {
			if (sc.hasNext()) next = sc.next();
			else eof = true;
			if (next.equals("")) eof = true;
			try {
				parseToken(next, type, sc);
				if (errors.size() > 0) {
					log.append(name).append("\n");
					for (String s : errors) {
						log.append("  ").append(s).append("\n");
					}
					log.append("---\n");
					errors.clear();
				}
			} catch (IllegalArgumentException
					| IllegalAccessException e) {
				log.append("could not parse token '"+ next+"'\n");
			}
		}
		return type;
	}
	
	
	private static class ParseErrorException extends Exception {
		String msg;
		public ParseErrorException(String string) {
			this.msg = string;
		}

		private static final long serialVersionUID = -7834101695477806091L;	
	}
	
	private static String getStringValue(Scanner sc) throws ParseErrorException {
		if (sc.hasNext()) {
			String value = sc.next();
			if (value.equals("")) errors.add("invalid String value");
			return value;
		}else {
			throw new ParseErrorException("Failed to parse String value.");
		}
	}
	
	private static float getFloatValue(Scanner sc) throws ParseErrorException {
		if (sc.hasNextFloat()) {
			float value = sc.nextFloat();
			return value;
		}else {
			throw new ParseErrorException("Failed to parse float value.");
		}
	}
	
	private static int getIntValue(Scanner sc) throws ParseErrorException {
		if (sc.hasNextFloat()) {
			int value = (int)sc.nextFloat();
			return value;
		}else {
			throw new ParseErrorException("Failed to parse int value.");
		}
	}
	
	private static boolean getBooleanValue(Scanner sc) throws ParseErrorException {
		if (sc.hasNextBoolean()) {
			boolean value = sc.nextBoolean();
			return value;
		}else if (sc.hasNext()) {
			String value = sc.next();
			if (value.toLowerCase().equals("yes")) {
				return true;
			}else if (value.toLowerCase().equals("no")) {
				return false;
			}else {
				throw new ParseErrorException("Invalid boolean value.");
			}
		}else {
			throw new ParseErrorException("Failed to parse boolean value.");
		}
	}

}
