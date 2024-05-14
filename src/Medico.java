import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Medico {

    private String nome;
    private String codigo;
    private List<Paciente> pacientes;

    public Medico(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
        this.pacientes = new ArrayList<>();

    }

    public void adicionarPaciente(Paciente paciente) {

        pacientes.add(paciente);

    }



    public String getNome() {
        return nome;
    }
    public String getCodigo() {
        return codigo;
    }
    public List<Paciente> getPacientes() {
        return pacientes;
    }

}
