package service;
import entity.Cours;
import entity.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import entity.Emargement;
import entity.JPAUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import java.io.File;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class EmargementImpl implements IRepository<Emargement> {

    private EntityManager getEntityManager() {
        return JPAUtil.getEntityManagerFactory().createEntityManager();
    }

    @Override
    public void add(Emargement obj) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Emargement obj) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Emargement obj) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Emargement emargement = em.find(Emargement.class, obj.getId());
            if (emargement != null) {
                em.remove(emargement);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public ObservableList<Emargement> getAll() {
        EntityManager em = getEntityManager();
        ObservableList<Emargement> list = FXCollections.observableArrayList();
        try {
            TypedQuery<Emargement> query = em.createQuery("SELECT e FROM Emargement e", Emargement.class);
            List<Emargement> resultList = query.getResultList();
            list.addAll(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return list;
    }

    @Override
    public Emargement getById(long id) {
        EntityManager em = getEntityManager();
        Emargement emargement = null;
        try {
            emargement = em.find(Emargement.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return emargement;
    }

    public void exportToExcel(List<Emargement> emargements) {
        // Création du workbook et de la feuille
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Emargements");

        // Création de l'en-tête
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Date");
        header.createCell(1).setCellValue("Professeur");
        header.createCell(2).setCellValue("Cours");
        header.createCell(3).setCellValue("Statut");

        // Format de date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Remplir les données
        for (int i = 0; i < emargements.size(); i++) {
            Emargement emargement = emargements.get(i);
            Row row = sheet.createRow(i + 1);  // Commencer à la ligne 1 pour les données
            row.createCell(0).setCellValue(dateFormat.format(emargement.getDate()));  // Format de la date
            row.createCell(1).setCellValue(emargement.getProfesseur().getNom());
            row.createCell(2).setCellValue(emargement.getCours().getNom());
            row.createCell(3).setCellValue(emargement.getStatut());
        }

        // Sauvegarder dans un fichier Excel
        try (FileOutputStream fileOut = new FileOutputStream("emargements.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Fermer le workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportToPdf(List<Emargement> emargements) {
        // Création du document PDF
        PDDocument document = new PDDocument();

        // Création d'une nouvelle page
        PDPage page = new PDPage();
        document.addPage(page);

        // Préparation du flux de contenu pour ajouter du texte sur la page
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(100, 750); // Position du texte sur la page

            // Titre
            contentStream.showText("Emargements");

            contentStream.newLineAtOffset(0, -20); // Saut de ligne pour l'en-tête
            contentStream.setFont(PDType1Font.HELVETICA, 10);

            // En-têtes des colonnes
            contentStream.showText("Date       | Professeur      | Cours       | Statut");

            contentStream.newLineAtOffset(0, -20); // Saut de ligne entre l'en-tête et les données

            // Format de date pour LocalDateTime
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Remplir les données
            for (Emargement emargement : emargements) {
                String date = emargement.getDate().format(dateFormatter); // Formater la date
                String professeur = emargement.getProfesseur().getNom();
                String cours = emargement.getCours().getNom();
                String statut = emargement.getStatut();

                // Ajout de la ligne de données
                contentStream.showText(date + " | " + professeur + " | " + cours + " | " + statut);
                contentStream.newLineAtOffset(0, -20); // Saut de ligne pour chaque ligne de données
            }

            contentStream.endText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sauvegarder le document PDF dans un fichier
        try {
            // Demander un emplacement de sauvegarde via une boîte de dialogue
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                document.save(file); // Sauvegarder dans le fichier sélectionné
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Fermer le document PDF
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public ObservableList<Emargement> getEmargementByProfesseur(User professeur) {
        EntityManager em = getEntityManager();
        try {
            List<Emargement> emList = em.createQuery(
                            "SELECT c FROM Emargement c WHERE c.professeur = :professeur", Emargement.class)
                    .setParameter("professeur", professeur)
                    .getResultList();
            return FXCollections.observableArrayList(emList);
        } finally {
            em.close();
        }
    }

}
