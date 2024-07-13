/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gestione.magazzinogui;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**            
 *
 * Questa classe contiene tutti i metodi che verranno utilizzati in seguito
 * nella GUI. La classe verrà utilizzata creando un nuovo oggetto nella GUI.
 * Questo è un compito assegnato per la materia di Informatica della classe 5IC.
 *
 * @author Nino Zullo
 * @since 22/01/24
 * @version 1.0.0 - Beta
 */
// TODO : - Implementare la possibilità di stampare un database intero
//        - Sistemare la chiusura dei database
//        - Migliorare UI

/*
         - Creo la Connessione 
         - Preparo la query :
         - Creo un oggetto PreparedStatement e gli passo la query
         - assegno i vari valori appena presi come parametri
         - Faccio eseguire la query e stampo con un messaggio se la query è andata a buon fine o no.
 */

public class Metodi {

    private final Map<String, Integer> fornitoriMap;

    /**
     * Costruttore che inizializza l'HashMap e carica i nomi dei fornitori dal
     * database
     */
    public Metodi() {
        fornitoriMap = new HashMap<>();
        caricaNomiFornitoriDaDatabase();
    }

    /**
     * Carica i nomi dei fornitori dal database e li aggiunge alla mappa
     * fornitoriMap.
     */
    public void caricaNomiFornitoriDaDatabase() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/GestioneMagazzinoGUI", "root", "")) {
            String query = "SELECT CodForn, Nome FROM Fornitori";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int codForn = rs.getInt("CodForn");
                        String nomeForn = rs.getString("Nome");
                        fornitoriMap.put(nomeForn, codForn);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Popola la ComboBox dei fornitori con i nomi dei fornitori presenti nel
     * database.
     *
     * @param cmbNomeForn ComboBox che rappresenta la lista dei nomi dei
     * fornitori.
     */
    public void popolaComboBoxFornitori(JComboBox<String> cmbNomeForn) {
        cmbNomeForn.removeAllItems();

        for (String nomeFornitore : fornitoriMap.keySet()) {
            cmbNomeForn.addItem(nomeFornitore);
        }
    }

    /**
     * Visualizza un messaggio informativo mediante una finestra di dialogo.
     *
     * @param titolo Titolo della finestra di dialogo.
     * @param messaggio Messaggio da visualizzare.
     */
    public void StampaMessaggio(String titolo, String messaggio) {
        JOptionPane.showMessageDialog(null, messaggio, titolo, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Passa al pannello "Inserisci" e carica i nomi dei fornitori dal database.
     *
     * @param pnlMain TabbedPane che contiene i pannelli principali
     * dell'applicazione.
     */
    public void VaiAdInserisci(JTabbedPane pnlMain) {
        pnlMain.setSelectedIndex(5);
        caricaNomiFornitoriDaDatabase();
    }

    /**
     * Torna al pannello principale (Home) nell'applicazione.
     *
     * @param pnlMain TabbedPane che contiene i pannelli principali
     * dell'applicazione.
     */
    public void TornaAllaHome(JTabbedPane pnlMain) {
        pnlMain.setSelectedIndex(0);
    }

    /**
     * Seleziona il pannello per l'inserimento nel databasr corrispondente in
     * base alla scelta dell'utente.
     *
     * @param SceltaUtente Scelta dell'utente tra "Seleziona", "Prodotti",
     * "Fornitori" o "Movimenti".
     * @param pnlMain TabbedPane che contiene i pannelli principali
     * dell'applicazione.
     */
    public void SelezionaDatabase(String SceltaUtente, JTabbedPane pnlMain) {

        switch (SceltaUtente) {

            case "Seleziona" ->
                StampaMessaggio("Errore", "Attenzione! Seleziona un database valido");
            case "Prodotti" ->
                pnlMain.setSelectedIndex(1);
            case "Fornitori" ->
                pnlMain.setSelectedIndex(3);
            case "Movimenti" ->
                pnlMain.setSelectedIndex(2);
        }
    }

    /**
     * Inserisce un nuovo prodotto nel database in base alla selezione della
     * visibilità del codice prodotto compresa di controlli e di CoomboBox che
     * stampa i nomi dei fornitori.
     *
     * @param txtCodProd TextField per l'inserimento del codice prodotto.
     * @param txtNome TextField per l'inserimento del nome del prodotto.
     * @param txtPrezzo TextField per l'inserimento del prezzo .
     * @param cmbNomeForn ComboBox per la selezione del nome del fornitore.
     * @param chbVisualizzaCodProdotto CheckBox per la selezione della
     * visibilità del codice prodotto.
     */
    public void InserisciProdotto(JTextField txtCodProd, JTextField txtNome, JTextField txtPrezzo, JComboBox<String> cmbNomeForn, JCheckBox chbVisualizzaCodProdotto) {
        if (chbVisualizzaCodProdotto.isSelected()) {
            try {
                int codProd = Integer.parseInt(txtCodProd.getText());
                String nome = txtNome.getText();
                String prezzoStr = txtPrezzo.getText();
                String nomeFornitore = cmbNomeForn.getSelectedItem().toString();

                if (nome.trim().isEmpty()) {
                    StampaMessaggio("Errore", "Il nome del prodotto non può essere vuoto.");
                    return;
                }

                float prezzo;
                try {
                    prezzo = Float.parseFloat(prezzoStr);
                } catch (NumberFormatException e) {
                    StampaMessaggio("Errore", "Il prezzo deve essere un numero valido.");
                    return;
                }

                Integer codForn = fornitoriMap.get(nomeFornitore);

                if (codForn == null) {
                    StampaMessaggio("Errore", "Fornitore non trovato.");
                    return;
                }

                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/GestioneMagazzinoGUI", "root", "")) {
                    String query = "INSERT INTO Prodotti (CodProd, Nome, Prezzo, CodForn) VALUES (?, ?, ?, ?)";

                    try (PreparedStatement pstmt = con.prepareStatement(query)) {
                        pstmt.setInt(1, codProd);
                        pstmt.setString(2, nome);
                        pstmt.setFloat(3, prezzo);
                        pstmt.setInt(4, codForn);

                        int righeInserite = pstmt.executeUpdate();

                        if (righeInserite > 0) {
                            StampaMessaggio("Inserimento riuscito", "Prodotto inserito con successo!");
                            txtCodProd.setText("");
                            txtNome.setText("");
                            txtPrezzo.setText("");
                        } else {
                            StampaMessaggio("Errore", "Nessuna riga inserita. Verificare l'operazione di inserimento.");
                        }
                    } catch (SQLIntegrityConstraintViolationException e) {
                        StampaMessaggio("Errore", "Chiave primaria duplicata. Verificare il codice prodotto inserito.");
                    }
                } catch (SQLException eccezione) {
                    eccezione.printStackTrace();
                }
            } catch (NumberFormatException e) {
                StampaMessaggio("Errore", "Il codice prodotto deve essere un numero valido.");
            }
        } else {
            String nome = txtNome.getText();
            String prezzoStr = txtPrezzo.getText();
            String nomeFornitore = cmbNomeForn.getSelectedItem().toString();

            if (nome.trim().isEmpty()) {
                StampaMessaggio("Errore", "Il nome del prodotto non può essere vuoto.");
                return;
            }

            float prezzo;
            try {
                prezzo = Float.parseFloat(prezzoStr);
            } catch (NumberFormatException e) {
                StampaMessaggio("Errore", "Il prezzo deve essere un numero valido.");
                return;
            }

            Integer codForn = fornitoriMap.get(nomeFornitore);

            if (codForn == null) {
                StampaMessaggio("Errore", "Fornitore non trovato.");
                return;
            }

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/GestioneMagazzinoGUI", "root", "")) {
                String query = "INSERT INTO Prodotti (Nome, Prezzo, CodForn) VALUES (?, ?, ?)";

                try (PreparedStatement pstmt = con.prepareStatement(query)) {
                    pstmt.setString(1, nome);
                    pstmt.setFloat(2, prezzo);
                    pstmt.setInt(3, codForn);

                    int righeInserite = pstmt.executeUpdate();

                    if (righeInserite > 0) {
                        StampaMessaggio("Inserimento riuscito", "Prodotto inserito con successo!");
                        txtCodProd.setText("");
                        txtNome.setText("");
                        txtPrezzo.setText("");
                    } else {
                        StampaMessaggio("Errore", "Nessuna riga inserita. Verificare l'operazione di inserimento.");
                    }
                } catch (SQLIntegrityConstraintViolationException e) {
                    StampaMessaggio("Errore", "Chiave primaria duplicata. Verificare il nome del prodotto inserito.");
                }
            } catch (SQLException eccezione) {
                eccezione.printStackTrace();
            }
        }
    }

    /**
     * Controlla e gestisce la visibilità della casella di testo del codice
     * prodotto in base allo stato della CheckBox.
     *
     * @param chbVisualizzaCodProdotto CheckBox per controllare la visibilità
     * del codice prodotto.
     * @param lblCodProd label del codice prodotto.
     * @param txtCodProd TextField per l'inserimento del codice prodotto.
     */
    public void ControllaCheckbox(JCheckBox chbVisualizzaCodProdotto, JLabel lblCodProd, JTextField txtCodProd) {
        if (chbVisualizzaCodProdotto.isSelected()) {
            lblCodProd.setVisible(true);
            txtCodProd.setVisible(true);
            StampaMessaggio("Avviso", "CodProd non è necessario in quanto è dotato di AUTO_INCREMENT, quindi in caso non viene fornito sarà fornito in automatico."
                    + "in caso si voglia omettere, disattiva la spunta!");
        } else {
            lblCodProd.setVisible(false);
            txtCodProd.setVisible(false);
        }
    }

    /**
     * Reimposta la selezione della ComboBox a "Seleziona" e mostra un messaggio
     * di conferma.
     *
     * @param cmbSelezionaDB ComboBox da resettare.
     */
    public void ResettaCheckbox(JComboBox cmbSelezionaDB) {
        cmbSelezionaDB.setSelectedIndex(0);
        StampaMessaggio("Azione Effettuata con successo", "L'opzione è stata resettata con successo");
    }

    /**
     * Mostra o nasconde la casella di testo del codice fornitore in base allo
     * stato del CheckBox.
     *
     * @param chbMostraCodForn CheckBox per controllare la visibilità del codice
     * fornitore.
     * @param lblCodForn label del codice fornitore.
     * @param txtCodFornF TextField per l'inserimento del codice fornitore.
     */
    public void MostraCodForn(JCheckBox chbMostraCodForn, JLabel lblCodForn, JTextField txtCodFornF) {
        if (chbMostraCodForn.isSelected()) {
            lblCodForn.setVisible(true);
            txtCodFornF.setVisible(true);
            StampaMessaggio("Avviso", "CodForn non è necessario in quanto è dotato di AUTO_INCREMENT, quindi in caso non viene fornito dall'utente sarà fornito in automatico."
                    + "in caso si voglia omettere, disattiva la spunta!");
        } else {
            lblCodForn.setVisible(false);
            txtCodFornF.setVisible(false);
        }
    }

    /**
     * Inserisce un nuovo fornitore nel database di gestione magazzino
     * effettuando i controlli delle caselle.
     *
     * @param txtNomeForn TextField contenente il nome del fornitore.
     * @param txtCittaForn TextField contenente la città del fornitore.
     * @param chbCodForn CheckBox per indicare se mostrarr il codice fornitore
     * (opzionale).
     * @param txtCodForn TextField contenente il codice fornitore (opzionale).
     */
    public void InserisciFornitori(JTextField txtNomeForn, JTextField txtCittaForn, JCheckBox chbCodForn, JTextField txtCodForn) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/GestioneMagazzinoGUI", "root", "")) {
            String query;
            if (chbCodForn.isSelected()) {
                query = "INSERT INTO Fornitori (CodForn, Nome, Citta) VALUES (?, ?, ?)";
            } else {
                query = "INSERT INTO Fornitori (Nome, Citta) VALUES (?, ?)";
            }

            String nomeFornitore = txtNomeForn.getText();
            String cittaFornitore = txtCittaForn.getText();

            if (nomeFornitore == null || nomeFornitore.trim().isEmpty()) {
                StampaMessaggio("Errore", "Inserisci un nome valido per il fornitore.");
                return;
            }

            if (cittaFornitore == null || cittaFornitore.trim().isEmpty()) {
                StampaMessaggio("Errore", "Inserisci una città valida.");
                return;
            }

            int codForn = 0;
            if (chbCodForn.isSelected()) {
                try {
                    codForn = Integer.parseInt(txtCodForn.getText());
                } catch (NumberFormatException e) {
                    StampaMessaggio("Errore", "Il codice del fornitore deve essere un numero valido.");
                    return;
                }
            }

            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                if (chbCodForn.isSelected()) {
                    pstmt.setInt(1, codForn);
                }
                pstmt.setString(chbCodForn.isSelected() ? 2 : 1, nomeFornitore);
                pstmt.setString(chbCodForn.isSelected() ? 3 : 2, cittaFornitore);

                try {
                    pstmt.executeUpdate();
                    StampaMessaggio("Inserimento riuscito", "Fornitore inserito con successo!");
                    txtNomeForn.setText("");
                    txtCittaForn.setText("");
                    txtCodForn.setText("");
                } catch (SQLIntegrityConstraintViolationException duplicateException) {
                    StampaMessaggio("Errore", "Duplicato: Questo fornitore è già presente nel database.");
                }
            }
        } catch (SQLException eccezione) {
            eccezione.printStackTrace();
            StampaMessaggio("Errore", "Si è verificato un errore durante l'inserimento del fornitore.");
        }
    }

    /**
     * Mostra o nasconde la casella ID e l'etichetta ID in base allo stato del
     * CheckBox.
     *
     * @param chbMostraID CheckBox : controlla la visibilità della TextField ID.
     * @param txtID TextField per l'input dell'ID.
     * @param lblID label dell'ID.
     */
    public void MostraCasellaID(JCheckBox chbMostraID, JTextField txtID, JLabel lblID) {
        if (chbMostraID.isSelected()) {
            txtID.setVisible(true);
            lblID.setVisible(true);
            StampaMessaggio("Avviso", "l'ID non è necessario in quanto è dotato di AUTO_INCREMENT, quindi in caso non viene fornito sarà fornito in automatico."
                    + "in caso si voglia omettere, disattiva la spunta!");
        } else {
            txtID.setVisible(false);
            lblID.setVisible(false);

        }

    }

    /**
     * Mostra la giacenza attuale dei prodotti filtrando per nome nel database
     * di gestione magazzino.
     *
     * @param txtNomeProdottoG TextField contenente il nome del prodotto per il
     * filtro.
     * @param tblCerca Tabella per visualizzare i risultati della giacenza
     * precedentemente Filtrata con il nomeProdotto.
     */
    public void MostraGiacenzaAttuale(JTextField txtNomeProdottoG, JTable tblCerca) {
        DefaultTableModel tblCercaModel;

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/GestioneMagazzinoGUI", "root", "")) {
            String query = "SELECT Prodotti.CodProd, Prodotti.Nome, "
                    + "SUM(CASE WHEN MOVIMENTI.Tipo = 'Carico' THEN MOVIMENTI.Quantita ELSE -MOVIMENTI.Quantita END) AS Giacenza "
                    + "FROM Prodotti "
                    + "LEFT JOIN MOVIMENTI ON Prodotti.CodProd = MOVIMENTI.CodProd "
                    + "WHERE Prodotti.Nome LIKE ? "
                    + "GROUP BY Prodotti.CodProd, Prodotti.Nome";
                    // riferimento al CASE https://www.w3schools.com/sql/sql_ref_case.asp  
            tblCercaModel = new DefaultTableModel();
            tblCercaModel.addColumn("Codice Prodotto");
            tblCercaModel.addColumn("Nome Prodotto");
            tblCercaModel.addColumn("Giacenza");
            tblCerca.setModel(tblCercaModel);

            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, "%" + txtNomeProdottoG.getText() + "%");

                try (ResultSet resultSet = pstmt.executeQuery()) {
                    while (resultSet.next()) {
                        int codiceProdotto = resultSet.getInt("CodProd");
                        String nomeProdotto = resultSet.getString("Nome");
                        int giacenza = resultSet.getInt("Giacenza");

                        tblCercaModel.addRow(new Object[]{codiceProdotto, nomeProdotto, giacenza});
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pulisce la TextField e la tabella dai campi precedentemente ottenuti.
     *
     * @param txtNomeProdottoG TextField checontiene il nome del prodotto.
     * @param tblCerca Tabella che mostrerà i prodotti.
     */
    public void PulisciGiacenza(JTextField txtNomeProdottoG, JTable tblCerca) {
        txtNomeProdottoG.setText("");
        DefaultTableModel model = (DefaultTableModel) tblCerca.getModel();
        model.setRowCount(0);
    }

    /**
     * Ci porta alla schermata dove visualizzare la Giacenza
     *
     * @param pnlMain Pannello principale per prendere il contesto.
     */
    public void VaiAGiacenza(JTabbedPane pnlMain) {
        pnlMain.setSelectedIndex(4);
    }

    /**
     * Inserisce un nuovo movimento nel database.
     *
     * @param spnData Spinner contenente la data del movimento.
     * @param txtQuantità TextField testo contenente la quantità del movimento.
     * @param txtCodProdM TextField contenente il codice prodotto del movimento.
     * @param cmbTipo ComboBox contenente il tipo del movimento (Carico o
     * Scarico).
     * @param txtID TextField contenente l'ID del movimento (opzionale).
     * @param chbMostraID CheckBox per indicare se mostrare l'ID del movimento.
     */
    public void InserisciMovimenti(JSpinner spnData, JTextField txtQuantità, JTextField txtCodProdM, JComboBox cmbTipo, JTextField txtID, JCheckBox chbMostraID) {
        try {
            java.util.Date data = (java.util.Date) spnData.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dataString = sdf.format(data);

            int quantita;
            int codProd;
            int id = 0;

            try {
                quantita = Integer.parseInt(txtQuantità.getText());
            } catch (NumberFormatException e) {
                StampaMessaggio("Errore", "La quantità deve essere un numero intero.");
                return;
            }

            try {
                codProd = Integer.parseInt(txtCodProdM.getText());
            } catch (NumberFormatException e) {
                StampaMessaggio("Errore", "Il codice prodotto deve essere un numero intero.");
                return;
            }

            String tipo = cmbTipo.getSelectedItem().toString();

            if (!tipo.equals("Carico") && !tipo.equals("Scarico")) {
                StampaMessaggio("Errore", "Il tipo deve essere 'Carico' o 'Scarico'.");
                return;
            }

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/GestioneMagazzinoGUI", "root", "")) {
                String query;

                if (chbMostraID.isSelected()) {

                    try {
                        id = Integer.parseInt(txtID.getText());
                    } catch (NumberFormatException e) {
                        StampaMessaggio("Errore", "L'ID deve essere un numero intero.");
                        return;
                    }

                    query = "INSERT INTO MOVIMENTI (Id, Data, Tipo, Quantita, CodProd) VALUES (?, ?, ?, ?, ?)";
                } else {
                    query = "INSERT INTO MOVIMENTI (Data, Tipo, Quantita, CodProd) VALUES (?, ?, ?, ?)";
                }

                try (PreparedStatement pstmt = con.prepareStatement(query)) {
                    if (chbMostraID.isSelected()) {
                        pstmt.setInt(1, id);
                        pstmt.setString(2, dataString);
                        pstmt.setString(3, tipo);
                        pstmt.setInt(4, quantita);
                        pstmt.setInt(5, codProd);
                    } else {
                        pstmt.setString(1, dataString);
                        pstmt.setString(2, tipo);
                        pstmt.setInt(3, quantita);
                        pstmt.setInt(4, codProd);
                    }

                    try {
                        pstmt.executeUpdate();
                        StampaMessaggio("Inserimento riuscito", "Movimento inserito con successo!");
                    } catch (SQLIntegrityConstraintViolationException e) {
                        if (e.getMessage().contains("Duplicate entry")) {
                            StampaMessaggio("Errore", "Movimento con ID duplicato. Assicurati che l'ID sia univoco.");
                        } else {
                            StampaMessaggio("Errore", "Assicurati che il CodProd esista nella tabella Prodotti.");
                        }
                    }
                }
            } catch (SQLException eccezione) {
                eccezione.printStackTrace();
                StampaMessaggio("Errore", "Si è verificato un errore durante l'inserimento del movimento.");
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
            StampaMessaggio("Errore", "Errore di conversione della data.");
        }
    }

    /**
     *
     * Metodo usato per uscire dal programma
     *
     */
    public void Esci() {
        StampaMessaggio("Arrivederci!", "Grazie mille per aver provato il mio programma! ");
        System.exit(0);
    }

}
