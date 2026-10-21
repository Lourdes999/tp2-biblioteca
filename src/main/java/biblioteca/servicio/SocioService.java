package biblioteca.servicio;

import biblioteca.modelo.Socio;

import java.util.ArrayList;
import java.util.List;

public class SocioService {
    private final List<Socio> socios = new ArrayList<>();
    private int proximoId = 1;

    public Socio registrarSocio(String dni, String nombre, String apellido, String correo, String telefono) {
        // RN01 / HU01: DNI, nombre y apellido son obligatorios
        if (estaVacio(dni)) {
            throw new IllegalArgumentException("El DNI es obligatorio");
        }
        // Corrección DEF01: el DNI debe contener solo números
        if (!dni.trim().matches("\\d+")) {
            throw new IllegalArgumentException("El DNI debe contener solo números");
        }
        if (estaVacio(nombre)) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (estaVacio(apellido)) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        // RN02: no puede haber dos socios con el mismo DNI
        if (buscarPorDni(dni) != null) {
            throw new IllegalArgumentException("Ya existe un socio con ese DNI");
        }
        Socio socio = new Socio(proximoId++, dni.trim(), nombre.trim(), apellido.trim(), correo, telefono);
        socios.add(socio);
        return socio;
    }

    public Socio buscarPorDni(String dni) {
        if (dni == null) return null;
        for (Socio s : socios) {
            if (s.getDni().equals(dni.trim())) {
                return s;
            }
        }
        return null;
    }

    public void cambiarEstado(String dni, boolean activo) {
        Socio socio = buscarPorDni(dni);
        if (socio == null) {
            throw new IllegalArgumentException("No existe un socio con ese DNI");
        }
        socio.setActivo(activo);
    }

    public List<Socio> listar() {
        return socios;
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
