import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MakeZipFile {
    public static void creerFichierZip(String fichierZipDestination) {
        try {
            // Flux de sortie vers le fichier ZIP
            FileOutputStream fos = new FileOutputStream(fichierZipDestination);
            ZipOutputStream zos = new ZipOutputStream(fos);

            // Récupérer le répertoire courant
            String repertoireCourant = System.getProperty("user.dir");

            // Parcourir les fichiers du répertoire courant
            File dossierCourant = new File(repertoireCourant);
            File[] fichiers = dossierCourant.listFiles();

            for (File fichier : fichiers) {
                if (fichier.isFile()) {
                    ajouterAuZip(zos, fichier, "");
                }
            }

            // Fermeture des flux
            zos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Fichier ZIP créé avec succès !");
    }

    private static void ajouterAuZip(ZipOutputStream zos, File fichier, String cheminDansZip) throws IOException {
        // Création de l'entrée ZIP pour le fichier
        ZipEntry zipEntry = new ZipEntry(cheminDansZip + fichier.getName());
        zos.putNextEntry(zipEntry);

        // Lecture du contenu du fichier et écriture dans le ZIP
        FileInputStream fis = new FileInputStream(fichier);
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, bytesRead);
        }

        // Fermeture de l'entrée et du flux de lecture
        fis.close();
        zos.closeEntry();
    }
}