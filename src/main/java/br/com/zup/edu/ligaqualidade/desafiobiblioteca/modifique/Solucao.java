package br.com.zup.edu.ligaqualidade.desafiobiblioteca.modifique;

import br.com.zup.edu.ligaqualidade.desafiobiblioteca.DadosDevolucao;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.DadosEmprestimo;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.EmprestimoConcedido;
import br.com.zup.edu.ligaqualidade.desafiobiblioteca.pronto.*;
import com.github.tomaslanger.chalk.Chalk;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Solucao {

	/**
	 * Você precisa implementar o código para executar o fluxo
	 * o completo de empréstimo e devoluções a partir dos dados
	 * que chegam como argumento. 
	 * 
	 * Caso você queira pode adicionar coisas nas classes que já existem,
	 * mas não pode alterar nada.
	 */
	
	/**
	 * 
	 * @param livros dados necessários dos livros
	 * @param exemplares tipos de exemplares para cada livro
	 * @param usuarios tipos de usuarios
	 * @param emprestimos informações de pedidos de empréstimos
	 * @param devolucoes informações de devoluções, caso exista. 
	 * @param dataParaSerConsideradaNaExpiracao aqui é a data que deve ser utilizada para verificar expiração
	 * @return
	 */
	public static Set<EmprestimoConcedido> executa(Set<DadosLivro> livros,
			Set<DadosExemplar> exemplares,
			Set<DadosUsuario> usuarios, Set<DadosEmprestimo> emprestimos,
			Set<DadosDevolucao> devolucoes, LocalDate dataParaSerConsideradaNaExpiracao) {

		ArrayList<EmprestimoConcedido> rents = new ArrayList<>();

		println("O usuário deseja realizar " + emprestimos.size() + " empréstimo(s).");

		emprestimos.stream().map( rentInfo -> {

			return null;
		});
		for (DadosEmprestimo rentInfo: emprestimos) {
			DadosExemplar copyFromBook = getCopyFromSetByBookId(exemplares, rentInfo.idLivro);
			DadosUsuario userFromRent = getUserFromSetByUserId(usuarios, rentInfo.idUsuario);
			println("Iniciando processamento de empréstimo do Livro [" + rentInfo.idLivro + "] com exemplar " +
					"de número: " + copyFromBook.idExemplar + " que está " + copyFromBook.tipo.name() + " para devolver em " + rentInfo.tempo + " dia(s)" +
					" para o usuário " + userFromRent.idUsuario + " que é " + userFromRent.padrao.name());
			EmprestimoConcedido rent = new EmprestimoConcedido(
					rentInfo.idUsuario,
					copyFromBook.idExemplar,
					getRentExpireDate(rentInfo.tempo)
			);
			if(validateRent(userFromRent, copyFromBook, rentInfo.tempo)) {
				println("Processamento de empréstimo do exemplar " + copyFromBook.idExemplar + " concluido com sucesso!", true);
				rents.add(rent);
			} else {
				println("Empréstimo não pode ser concluido.", false);
			}
		}

		println("O usuário conseguiu concluir " + rents.size() + " empréstimo(s) com sucesso.");

		return new HashSet<>(rents);
	}

	private static boolean validateRent(DadosUsuario user, DadosExemplar copy, int date){
		boolean validation = false;
		if(date <= 60) {
			if (copy.tipo == TipoExemplar.RESTRITO && user.padrao == TipoUsuario.PESQUISADOR)
				validation = true;
			if (copy.tipo == TipoExemplar.LIVRE)
				validation = true;
		}
		return validation;
	}

	private static DadosExemplar getCopyFromSetByBookId(Set<DadosExemplar> fromCopies, int byBookId) {
		DadosExemplar returnCopy = new DadosExemplar(TipoExemplar.LIVRE, -1, -1);
		for (DadosExemplar copy: fromCopies) {
			if (byBookId == copy.idLivro) {
				returnCopy = copy;
				break;
			}
		}
		return returnCopy;
	}

	private static DadosUsuario getUserFromSetByUserId(Set<DadosUsuario> fromUsers, int byUserId) {
		DadosUsuario returnUser = new DadosUsuario(TipoUsuario.PADRAO, -1);
		for (DadosUsuario user :
				fromUsers) {
			if(byUserId == user.idUsuario)
				returnUser = user;
			break;
		}
		return returnUser;
	}

	private static LocalDate getRentExpireDate(int date) {
		return LocalDate.now().plusDays(date);
	}

	private static void println(String message) {
		 System.out.println(message);
	}

	private static void println(String message, boolean status) {
		if (status)
			System.out.println(Chalk.on(message).green());
		else
			System.out.println(Chalk.on(message).red());
	}

}
