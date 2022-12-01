package storage_masters.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	/**
	 * Jag löste fil-sparningen genom att ha en entity som hade fält för
	 * fileName
	 * fileType
	 * data (byteArray)
	 * user ( User object ),
	 * När jag kallar på min upload endpoint så tar jag in ett userObject samt
	 * en MultipartFile som finns inbyggd i Spring-web,
	 * Därefter skickar jag vidare den till FileServicen där man med en konstruktor sätter entitetens
	 * värde med hjälp av MultipartFile/Spring-webs funktioner såsom
	 * //
	 * file.getOriginalFileName()
	 * file.getContentType()
	 * file.getBytes() --> Denna lägger filens data i den bytesArrayen i UserFile-klassen
	 * //(Sen också user.getUser() för att få ut en User från userObject)
	 *
	 * Efter detta kör jag en fileRepository.save(userFile) där jag skickar med hela filen så att repositoryn
	 * kan lägga in den i databasen.
	 * För att koppa filen till en användare har jag lagt in en @ManyToOne på en User user i UserFile-klassen
	 * där man skickar in en användare (Som vi plockar ut ur jwt-token --> userObject --> User)
	 *
	 * Jag tyckte att detta sättet att ladda upp filer osv var ganska bra och enkelt att hantera, Jag är inte så
	 * kunnig om bytes och så vidare men det känns som att ladda upp en fil genom att plocka alla bytes och lägga de
	 * i en array är det smidigaste sättet att ladda upp filer till sin databas.
	 *
	 * I och med att detta projektet inte är så stort och vi inte har extremt mycket tid så tror jag inte att där är
	 * så många sätt man kan optimera koden. Det finns säkert lite olika optimeringar som man kan hitta om man
	 * har mer tid att arbeta men ingenting som jag kommer att hinna med i detta projektet.
	 *
	 *
	 *
	 */

}
