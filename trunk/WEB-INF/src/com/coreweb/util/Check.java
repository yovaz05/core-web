package com.coreweb.util;

import java.util.Date;

import org.zkoss.lang.Classes;
import org.zkoss.util.Dates;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.CustomConstraint;
import org.zkoss.zul.SimpleConstraint;

import com.coreweb.IDCore;
import com.coreweb.control.GenericViewModel;
import com.coreweb.domain.Domain;
import com.coreweb.domain.IiD;
import com.coreweb.domain.Register;

public class Check {

	public static String MENSAJE_NO_EMPTY = "Este campo no debe quedar vacio";
	public static String MENSAJE_NO_FUTURO = "Fecha mayor a la actual no permitida";
	public static String MENSAJE_NO_PRESENTE = "Fecha actual no permitida";
	public static String MENSAJE_NO_PASADO = "Fecha menor a la actual no permitida";
	public static String MENSAJE_NO_CERO = "No se permite valor cero en este campo";
	public static String MENSAJE_NO_POSITIVO = "No se permite valor mayor a cero en este campo";
	public static String MENSAJE_NO_NEGATIVO = "No se permite valor negativo en este campo";
	public static String MENSAJE_MAYOR_A = "El valor debe ser mayor a ";
	public static String MENSAJE_MENOR_A = "El valor debe ser menor a ";
	public static String MENSAJE_MAYOR_IGUAL_A = "El valor debe ser mayor o igual a ";
	public static String MENSAJE_MENOR_IGUAL_A = "El valor debe ser menor o igual a ";
	public static String MENSAJE_RUC = "Debe Ingresar un Ruc valido\nEl RUC debe tener el guión ej: 9999999-9";
	public static String MENSAJE_EMAIL = "Debe Ingresar un correo valido";
	public static String TRUE_FALSE = "Debe Ingresar \nT para Verdadero, o \nF para Falso ";

	private GenericViewModel vm = null;

	public Check(GenericViewModel vm) {
		this.vm = vm;
	}

	// No admite empty o null
	public MiConstraint getNoVacio() {
		return new MiConstraint(this.vm, MiConstraint.NO_EMPTY);
	}

	// No admite fecha mayor a la actual
	public MiConstraint getNoFuturo() {
		return new MiConstraint(this.vm, MiConstraint.NO_FUTURO);
	}

	// No admite fecha menor a la actual
	public MiConstraint getNoPasado() {
		return new MiConstraint(this.vm, MiConstraint.NO_PASADO);
	}

	// No admite fecha actual
	public MiConstraint getNoPresente() {
		return new MiConstraint(this.vm, MiConstraint.NO_PRESENTE);
	}

	// No admite valor mayor a cero
	public MiConstraint getNoPositivo() {
		return new MiConstraint(this.vm, MiConstraint.NO_POSITIVO);
	}

	// No admite valor cero
	public MiConstraint getNoCero() {
		return new MiConstraint(this.vm, MiConstraint.NO_CERO);
	}

	// No admite valores menores a cero
	public MiConstraint getNoNegativo() {
		return new MiConstraint(this.vm, MiConstraint.NO_NEGATIVO);
	}

	// Solo admite valor mayor a ?
	public MiConstraint mayorA(Number value) {
		return new MiConstraint(this.vm, MiConstraint.MAYOR_A, value);
	}

	// Solo admite valor menor a ?
	public MiConstraint menorA(Number value) {
		return new MiConstraint(this.vm, MiConstraint.MENOR_A, value);
	}

	// Solo admite valor mayor o igual a ?
	public MiConstraint mayorIgualA(Number value) {
		return new MiConstraint(this.vm, MiConstraint.MAYOR_IGUAL_A, value);
	}

	// Solo admite valor menor o igual a ?
	public MiConstraint menorIgualA(Number value) {
		return new MiConstraint(this.vm, MiConstraint.MENOR_IGUAL_A, value);
	}

	// Valida el digito verificador del ruc
	public MiConstraint getRuc() {
		return new MiConstraint(this.vm, MiConstraint.RUC);
	}

	// Valida el formato del correo
	public MiConstraint getEmail() {
		return new MiConstraint(this.vm, MiConstraint.EMAIL);
	}

	// Valida el formato de varios correos separados con ';'
	public MiConstraint getEmails() {
		return new MiConstraint(this.vm, MiConstraint.EMAILS);
	}

	// Valida el formato del correo
	public MiConstraint getTrueFalse() {
		return new MiConstraint(this.vm, MiConstraint.TRUE_FALSE);
	}

}

class MiConstraint extends SimpleConstraint implements Constraint,
		CustomConstraint {

	public static final int NO_EMPTY = 1;
	public static final int NO_FUTURO = 2;
	public static final int NO_PASADO = 3;
	public static final int NO_PRESENTE = 4;
	public static final int NO_POSITIVO = 5;
	public static final int NO_CERO = 6;
	public static final int NO_NEGATIVO = 7;
	public static final int MAYOR_A = 8;
	public static final int MENOR_A = 9;
	public static final int MAYOR_IGUAL_A = 10;
	public static final int MENOR_IGUAL_A = 11;
	public static final int RUC = 12;
	public static final int EMAIL = 13;
	public static final int EMAILS = 14;
	public static final int TRUE_FALSE = 15;

	private int constraint = 0;
	private Number value = 0;
	private Misc misc = new Misc();
	private GenericViewModel vm = null;

	public MiConstraint(GenericViewModel vm, int constraint, Number value) {
		super("");
		this.vm = vm;
		this.constraint = constraint;
		this.value = value;
	}

	public MiConstraint(GenericViewModel vm, int constraint) {
		super("");
		this.vm = vm;
		this.constraint = constraint;
	}

	private static int compareTo(Comparable v1, Object v2) {
		return v1.compareTo(v2);
	}

	@Override
	public void validate(Component comp, Object value)
			throws WrongValueException {
		try {

			if (this.vm.isDeshabilitado() == true) {
				return;
			}

			MiValidate(comp, value);

		} catch (WrongValueException ex) {
			/*
			 * if (comp instanceof InputElement){ InputElement im =
			 * (InputElement)comp; String st = im.getStyle();
			 * im.setStyle(st+";border: 1px solid red"); }
			 */

			Clients.showNotification(ex.getMessage(), "error", comp,
					"end_center", 3000, true);

		}

	}

	public void MiValidate(Component comp, Object value)
			throws WrongValueException {

		if (value == null) {
			throw new WrongValueException(comp, Check.MENSAJE_NO_EMPTY);

			// Check de Numeros
		} else if (value instanceof Number) {
			final int cmp = compareTo((Comparable) value,
					Classes.coerce(value.getClass(), null, false)); // compara
																	// con cero
			if (cmp > 0) {
				if (this.constraint == this.NO_POSITIVO) {
					throw new WrongValueException(comp,
							Check.MENSAJE_NO_POSITIVO);
				}
			} else if (cmp == 0) {
				if (this.constraint == this.NO_CERO) {
					throw new WrongValueException(comp, Check.MENSAJE_NO_CERO);
				}
			} else {
				if (this.constraint == this.NO_NEGATIVO) {
					throw new WrongValueException(comp,
							Check.MENSAJE_NO_NEGATIVO);
				}
			}

			if ((this.constraint == this.MAYOR_A)
					&& (misc.compararNumeros((Number) value, this.value) != 1)) {
				throw new WrongValueException(comp, Check.MENSAJE_MAYOR_A
						+ this.value);
			}
			if ((this.constraint == this.MENOR_A)
					&& (misc.compararNumeros((Number) value, this.value) != -1)) {
				throw new WrongValueException(comp, Check.MENSAJE_MENOR_A
						+ this.value);
			}
			if ((this.constraint == this.MAYOR_IGUAL_A)
					&& (misc.compararNumeros((Number) value, this.value) == -1)) {
				throw new WrongValueException(comp, Check.MENSAJE_MAYOR_IGUAL_A
						+ this.value);
			}
			if ((this.constraint == this.MENOR_IGUAL_A)
					&& (misc.compararNumeros((Number) value, this.value) == 1)) {
				throw new WrongValueException(comp, Check.MENSAJE_MENOR_IGUAL_A
						+ this.value);
			}

			// Check de String
		} else if (value instanceof String) {
			String s = (String) value;
			if (this.constraint == this.NO_EMPTY && s.isEmpty()) {
				throw new WrongValueException(comp, Check.MENSAJE_NO_EMPTY);
			}
			if (this.constraint == RUC) {
				Ruc ruc = new Ruc();
				// ver si es un ruc valido
				if (ruc.validarRuc(s) == false) {
					throw new WrongValueException(comp, Check.MENSAJE_RUC);
				}
				// ver que no sea un ruc repetido
				try {
					Class empresa = (Class) this.misc.ejecutarMetoto(this.vm,
							"getRucEmpresa");
					IiD id = (IiD) this.misc.ejecutarMetoto(this.vm,
							"getIdEmpresa");
					Register rr = Register.getInstance();
					if (rr.existe(empresa, "ruc", IDCore.TIPO_STRING, value, id)) {
						throw new WrongValueException(comp,
								"El RUC '"+value+"' ya existe");
					}

				} catch (WrongValueException e) {
					throw e;
				} catch (Exception e) {
					e.printStackTrace();
					throw new WrongValueException(comp,
							"Error en metodo getRucEmpresa");
				}

			}
			if ((this.constraint == this.EMAIL)
					&& (misc.checkEmail(s) == false)) {
				throw new WrongValueException(comp, Check.MENSAJE_EMAIL);
			}
			if ((this.constraint == this.EMAILS)
					&& ((misc.chequearMultipleCorreos(s.trim().split(";")) == false) || (((String) value)
							.isEmpty()))) {
				throw new WrongValueException(comp, Check.MENSAJE_EMAIL);
			}
			if ((this.constraint == this.TRUE_FALSE)
					&& (misc.checkTrueFalse(s) == false)) {
				throw new WrongValueException(comp, Check.TRUE_FALSE);
			}

			// Check de Fechas
		} else if (value instanceof Date) {
			final Date date = Dates.beginOfDate((Date) value, null);
			final int cmp = date.compareTo(Dates.today());
			if (cmp > 0) {
				if (this.constraint == this.NO_FUTURO) {
					throw new WrongValueException(comp, Check.MENSAJE_NO_FUTURO);
				}
			} else if (cmp == 0) {
				if (this.constraint == this.NO_PRESENTE) {
					throw new WrongValueException(comp,
							Check.MENSAJE_NO_PRESENTE);
				}
			} else {
				if (this.constraint == this.NO_PASADO) {
					throw new WrongValueException(comp, Check.MENSAJE_NO_PASADO);
				}
			}
		}
	}

	public void showCustomError(Component comp, WrongValueException ex) {
		if (ex != null) {
			Clients.showNotification(ex.getMessage(), "error", comp,
					"end_center", 3000, true);
		}
	}

}