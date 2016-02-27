package beans;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MigrarInformacion {
 
	public int migrarInformacion(String idArchivo) throws IOException, BiffException {
		Conexion conexion = new Conexion();
		String archivo = "";
		ResultSet rs2 = conexion.consultarBD("SELECT archivo FROM archivos_planos WHERE id_archivo=" + idArchivo);
		try {
			if (rs2.next()) {
				archivo = rs2.getString(1);
			}
			rs2.close();
		} catch (SQLException e) {
			//e.printStackTrace();
		}

		Parametro parametro = new Parametro();
		String[] parametros = parametro.getarametros();
		String aRutaArchivoExcel = parametros[5] + archivo;
		String mensaje = "";
		int numeroMigrados = 0;

		if (!archivo.equals("")) {

			List<String> informacion = new ArrayList<String>();

			Workbook workbook = Workbook.getWorkbook(new File(aRutaArchivoExcel));
			Sheet sheet = workbook.getSheet(0);// Hoja 0
			Cell celdaCurso = null;
			String valorCeldaCurso = null;

			conexion.setAutoCommitBD(false);
			int sw = 0;
			int numeroRegistros = sheet.getRows();

			for (int filaActual = 1; filaActual <= numeroRegistros - 1; filaActual++) {
				sw = 0;
				if (informacion != null && informacion.size() > 0) {
					informacion.clear();
				}

				celdaCurso = sheet.getCell(0, filaActual);// col, fila
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(1, filaActual);
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(2, filaActual);
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(3, filaActual);
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(4, filaActual);
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(5, filaActual);
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(6, filaActual);
				valorCeldaCurso = celdaCurso.getContents();

				if (valorCeldaCurso != null) {
					String[] fecha = valorCeldaCurso.split("/");
					valorCeldaCurso = fecha[2] + "-" + fecha[0] + "-" + fecha[1];
				}

				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(7, filaActual);
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(8, filaActual);
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(9, filaActual);
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(10, filaActual);
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(11, filaActual);
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				celdaCurso = sheet.getCell(12, filaActual);
				valorCeldaCurso = celdaCurso.getContents();
				informacion.add(valorCeldaCurso);

				ResultSet rs = conexion.consultarBD("SELECT * FROM egresados WHERE codigo_estudiante=" + informacion.get(0));
				try {
					if (rs.next()) {

						sw = 1;
					} else {
						numeroMigrados++;
					}
					rs.close();
				} catch (SQLException e) {
					//e.printStackTrace();
				}

				if (sw == 0) {
					conexion
							.actualizarBD("INSERT INTO egresados(codigo_estudiante, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, documento, fecha_nacimiento, direccion_actual, sexo, telefonos_fijos, correo, ano_grado, periodo_grado) values('"
									+ informacion.get(0) + "','" + informacion.get(1) + "','" + informacion.get(2) + "','" + informacion.get(3) + "','" + informacion.get(4) + "','" + informacion.get(5) + "','" + informacion.get(6) + "','" + informacion.get(7) + "','"
									+ informacion.get(8) + "','" + informacion.get(9) + "','" + informacion.get(10) + "','" + informacion.get(11) + "','" + informacion.get(12) + "')");
				}

			}
			if (numeroRegistros > 0) {
				mensaje = "Información migrada con éxito";
				conexion.commitBD();
			} else {
				conexion.rollbackBD();
				mensaje = "No existe información o información en formato incorrecto(por ejemplo fechas errones).Revise la información del archivo de Excel";
			}

			workbook.close();

		}
		conexion.cerrarConexion();

		return numeroMigrados;
	}

	public static void main(String[] args) {
		MigrarInformacion migracionInformacion = new MigrarInformacion();
		try {
			migracionInformacion.migrarInformacion("C:\\DSI_grados.xls");
		} catch (BiffException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
}
