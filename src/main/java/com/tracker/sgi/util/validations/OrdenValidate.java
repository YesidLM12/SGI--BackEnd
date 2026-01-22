package com.tracker.sgi.util.validations;

import com.tracker.sgi.dto.request.DetallesRequestDto;
import com.tracker.sgi.dto.request.OrdenRequestDto;
import com.tracker.sgi.exception.InvalidDataException;

public class OrdenValidate {

	public static void validate (DetallesRequestDto dto) {
		if (dto.cantidad() < 0) {
			throw new InvalidDataException("La cantidad debe ser mayor o igual a 0");
		}
	}
}
