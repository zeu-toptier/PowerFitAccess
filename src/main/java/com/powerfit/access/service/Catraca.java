package com.powerfit.access.service;

import com.powerfit.access.model.RegistroAcesso;
import com.powerfit.access.model.ResultadoAcesso;

public class Catraca {

    public void processar(RegistroAcesso registro) {

        if (registro == null) {
            throw new IllegalArgumentException(
                    "O registro de acesso é obrigatório."
            );
        }

        System.out.println();
        System.out.println("========== CATRACA ==========");

        if (registro.getResultado() == ResultadoAcesso.AUTORIZADO) {
            System.out.println("ACESSO LIBERADO!");
            System.out.println("A catraca foi desbloqueada.");
        } else {
            System.out.println("ACESSO NEGADO!");
            System.out.println("A catraca permanece bloqueada.");
        }

        System.out.println("Matrícula: " + registro.getNumeroMatricula());
        System.out.println("Motivo: " + registro.getMotivo());
        System.out.println("=============================");
        System.out.println();
    }
}
