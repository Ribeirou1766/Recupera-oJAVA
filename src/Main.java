import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.StandardSocketOptions;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Integer.parseInt;

public class Main {

    public static List<Medico> lerMedicos(String caminhoCsv) {

        List<Medico> medicos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoCsv))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");

                if (dados.length == 2) {
                    String nome = dados[0];
                    String codigo = dados[1];
                    medicos.add(new Medico(nome, codigo));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return medicos;




    }

    public static List<Paciente> lerPacientes( String caminhoCsv) {

        List<Paciente> pacientes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoCsv))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");

                if (dados.length == 2) {
                    String nome = dados[0];
                    String codigo = dados[1];
                    pacientes.add(new Paciente(nome, codigo));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pacientes;
    }

    public static Medico buscarMedico(List<Medico> medicos, String codigo) {

        for(Medico medico: medicos) {

            if (medico.getCodigo().equals(codigo)){
                return medico;
            }
        }
        return null;


    }

    public static Paciente buscarPaciente(List<Paciente> pacientes, String cpf) {

        for(Paciente paciente: pacientes) {
            if (paciente.getCpf().equals(cpf)){
                return paciente;
            }
        }
        return null;


    }
        public static List<Consulta> lerConsultas( String caminhoCsv, List<Medico> medicos, List<Paciente> pacientes ) {

            List<Consulta> consultas = new ArrayList<>();



            try (BufferedReader br = new BufferedReader(new FileReader(caminhoCsv))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    String[] dados = linha.split(",");

                    if (dados.length == 4) {
                        LocalDate data = LocalDate.parse(dados[0]);
                        LocalTime horario = LocalTime.parse(dados[1]);
                        String codigo = dados[2];
                        String cpf = dados[3];
                        Medico medico = buscarMedico(medicos, codigo);
                        Paciente paciente = buscarPaciente(pacientes, cpf);
                        Consulta consulta = new Consulta(data,horario,medico,paciente);
                        if(paciente != null){
                            medico.adicionarPaciente(paciente);
                            paciente.adicionarConsulta(consulta);
                        }




                        consultas.add(consulta);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return consultas;



    }


    public static void main(String[] args) {

        List<Medico> medicos = lerMedicos("C:\\Users\\User\\IdeaProjects\\Recuperacao_POO_Bruno_e_Otavio\\src\\Medicos.csv");
        List<Paciente> listaPacientes = lerPacientes("C:\\Users\\User\\IdeaProjects\\Recuperacao_POO_Bruno_e_Otavio\\src\\Pacientes.csv");
        List<Consulta> listaConsultas = lerConsultas("C:\\Users\\User\\IdeaProjects\\Recuperacao_POO_Bruno_e_Otavio\\src\\Consultas.csv", medicos, listaPacientes);



        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem vindo ao banco de dados do Hospital");
        System.out.println("1- Listar todos os pacientes de um determinado médico ");
        System.out.println("2- Listar todas as consultas agendadas por um determinado médico");
        System.out.println("3- Listar todos os médicos de um determinado paciente");
        System.out.println("4- Listar todas as consultas que um determinado paciente realizou");
        System.out.println("5- Listar todas as consultas agendadas que um determinado paciente possui");
        System.out.println("6- Listar quais pacientes um determinado médico que não o cosulta há mais" +
                           " de um determiando tempo");

        int opcao = scanner.nextInt();

        switch (opcao) {

            case 1:


                System.out.println("Informe o Registro do médico");

                boolean codigoExiste = false;

                while (!codigoExiste) {

                    String codigo = scanner.next();

                    Medico medico = buscarMedico(medicos, codigo);

                    if (medico == null) {
                        System.out.println("Código inválido, tente novamente.");
                    }
                    else {
                        codigoExiste = true;
                    }

                    for(Paciente paciente: medico.getPacientes()){

                        System.out.println("Nome: " + paciente.getNome());
                    }















                }





                break;
            case 2:

                System.out.println("Informe o Registro do médico");


                codigoExiste = false;

                Medico medico = null;

                while (!codigoExiste) {


                    String codigo = scanner.next();

                    medico = buscarMedico(medicos, codigo);

                    if (medico == null) {
                        System.out.println("Código inválido, tente novamente.");
                    }
                    else {
                        codigoExiste = true;
                    }
                }


                System.out.println("Informe a Data inicial (DD/MM/YYYY): ");

                String dataInicial = scanner.next();

                System.out.println("Informe a Data final (DD/MM/YYYY): ");

                String dataFinal = scanner.next();

                List<Consulta> consultasAgendadas = new ArrayList<>();


                for (Paciente paciente : medico.getPacientes()) {

                    for(Consulta consulta: paciente.getConsultas()) {
                        LocalDate dataConsulta = consulta.getData();
                        String[] dataInicialSplitted = dataInicial.split("/");
                        String[] dataFinalSplitted = dataFinal.split("/");
                        if(!dataConsulta.isBefore(LocalDate.of(parseInt(dataInicialSplitted[2]),parseInt(dataInicialSplitted[1]), parseInt(dataInicialSplitted[0]))) && (!dataConsulta.isAfter(LocalDate.of(parseInt(dataFinalSplitted[2]),parseInt(dataFinalSplitted[1]), parseInt(dataFinalSplitted[0]))))) {

                            consultasAgendadas.add(consulta);



                        }
                    }




                }

                consultasAgendadas.sort( (c1,c2) -> c1.getHorario().compareTo(c2.getHorario()));

                for(Consulta consulta: consultasAgendadas){
                    System.out.println(consulta.getData());
                }


                break;
            case 3:

                System.out.println("Informe o CPF do paciente: ");

                codigoExiste = false;

                while (!codigoExiste) {

                    String cpf = scanner.next();

                    Paciente paciente = buscarPaciente(listaPacientes, cpf);

                    if (cpf == null) {
                        System.out.println("Código inválido, tente novamente.");
                    } else {
                        codigoExiste = true;
                    }

                    for (Consulta consulta : paciente.getConsultas()) {

                        System.out.println("Nome: " + consulta.getMedico().getNome());
                    }


                }
                break;

            case 4:

                System.out.println("Informe o CPF do paciente: ");

                codigoExiste = false;

                while (!codigoExiste) {

                    String cpf = scanner.next();

                    Paciente paciente = buscarPaciente(listaPacientes, cpf);

                    if (cpf == null) {
                        System.out.println("Código inválido, tente novamente.");
                    } else {
                        codigoExiste = true;
                    }

                    for (Consulta consulta : paciente.getConsultas()) {


                        if(consulta.getData().isBefore(LocalDate.now())){

                            System.out.println("Nome: " + consulta.getMedico().getNome()+ ", dia: "+ consulta.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                        }


                    }


                }
                break;
            case 5:

                System.out.println("Informe o CPF do paciente: ");

                codigoExiste = false;

                while (!codigoExiste) {

                    String cpf = scanner.next();

                    Paciente paciente = buscarPaciente(listaPacientes, cpf);

                    if (cpf == null) {
                        System.out.println("Código inválido, tente novamente.");
                    } else {
                        codigoExiste = true;
                    }

                    for (Consulta consulta : paciente.getConsultas()) {


                        if (consulta.getData().isAfter(LocalDate.now())) {

                            System.out.println("Nome: " + consulta.getMedico().getNome() + ", dia: " + consulta.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                        }


                    }
                }
                break;


            case 6:

                System.out.println("Informe o CPF do paciente: ");

                codigoExiste = false;

                while (!codigoExiste) {

                    String cpf = scanner.next();

                    Paciente paciente = buscarPaciente(listaPacientes, cpf);

                    if (cpf == null) {
                        System.out.println("Código inválido, tente novamente.");
                    } else {
                        codigoExiste = true;
                    }

                    System.out.print("Informe o periodo em Meses: ");

                    int meses = scanner.nextInt();

                    for (Consulta consulta : paciente.getConsultas()) {


                        Period periodo = Period.between(consulta.getData(), LocalDate.now());
                        int mesesDesdeAConsulta = periodo.getYears() * 12 + periodo.getMonths();


                        if (mesesDesdeAConsulta >= meses) {

                            System.out.println("Nome: " + consulta.getMedico().getNome() + ", dia: " + consulta.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                        }


                    }
                }

                break;
            default:
                System.out.println("Opção inválida!");
                break;

        }




        }

    }




