package com.example.bibliothequeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLivres;
    private FloatingActionButton fabAjouterLivre;

    private LivreAdapter livreAdapter;
    private List<Livre> listeLivres;

    private AppDatabase database;
    private ExecutorService executorService;

    private ActivityResultLauncher<Intent> addEditLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewLivres = findViewById(R.id.recyclerViewLivres);
        fabAjouterLivre = findViewById(R.id.fabAjouterLivre);

        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        listeLivres = new ArrayList<>();

        livreAdapter = new LivreAdapter(listeLivres, new LivreAdapter.OnLivreClickListener() {
            @Override
            public void onLivreClick(Livre livre) {
                ouvrirDetailLivre(livre);
            }

            @Override
            public void onLivreLongClick(Livre livre, int position) {
                afficherOptionsLivre(livre);
            }
        });

        recyclerViewLivres.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewLivres.setAdapter(livreAdapter);

        initialiserActivityResultLauncher();

        fabAjouterLivre.setOnClickListener(v -> ouvrirFormulaireAjout());

        chargerLivresDepuisRoom();
    }

    private void initialiserActivityResultLauncher() {
        addEditLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Livre livre = (Livre) data.getSerializableExtra(AddEditActivity.EXTRA_LIVRE);
                        String mode = data.getStringExtra(AddEditActivity.EXTRA_MODE);

                        if (livre == null) return;

                        if (AddEditActivity.MODE_ADD.equals(mode)) {
                            ajouterLivreDansRoom(livre);
                        } else if (AddEditActivity.MODE_EDIT.equals(mode)) {
                            modifierLivreDansRoom(livre);
                        }
                    }
                }
        );
    }

    private void chargerLivresDepuisRoom() {
        executorService.execute(() -> {
            List<Livre> livresDepuisBase = database.livreDao().getAllLivres();
            runOnUiThread(() -> {
                listeLivres.clear();
                listeLivres.addAll(livresDepuisBase);
                livreAdapter.notifyDataSetChanged();
            });
        });
    }

    private void ajouterLivreDansRoom(Livre livre) {
        executorService.execute(() -> {
            livre.setId(0);
            database.livreDao().insert(livre);
            runOnUiThread(() -> {
                Toast.makeText(this, "✅ Livre ajouté !", Toast.LENGTH_SHORT).show();
                chargerLivresDepuisRoom();
            });
        });
    }

    private void modifierLivreDansRoom(Livre livre) {
        executorService.execute(() -> {
            database.livreDao().update(livre);
            runOnUiThread(() -> {
                Toast.makeText(this, "✏️ Livre modifié !", Toast.LENGTH_SHORT).show();
                chargerLivresDepuisRoom();
            });
        });
    }

    private void supprimerLivreDansRoom(Livre livre) {
        executorService.execute(() -> {
            database.livreDao().delete(livre);
            runOnUiThread(() -> {
                Toast.makeText(this, "🗑️ Livre supprimé !", Toast.LENGTH_SHORT).show();
                chargerLivresDepuisRoom();
            });
        });
    }

    private void ouvrirFormulaireAjout() {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        intent.putExtra(AddEditActivity.EXTRA_MODE, AddEditActivity.MODE_ADD);
        addEditLauncher.launch(intent);
    }

    private void ouvrirFormulaireModification(Livre livre) {
        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
        intent.putExtra(AddEditActivity.EXTRA_MODE, AddEditActivity.MODE_EDIT);
        intent.putExtra(AddEditActivity.EXTRA_LIVRE, livre);
        addEditLauncher.launch(intent);
    }

    private void ouvrirDetailLivre(Livre livre) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("livre", livre);
        startActivity(intent);
    }

    private void afficherOptionsLivre(Livre livre) {
        String[] options = {"Modifier", "Supprimer"};
        new AlertDialog.Builder(this)
                .setTitle(livre.getTitre())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        ouvrirFormulaireModification(livre);
                    } else if (which == 1) {
                        confirmerSuppression(livre);
                    }
                })
                .show();
    }

    private void confirmerSuppression(Livre livre) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer le livre")
                .setMessage("Voulez-vous vraiment supprimer ce livre ?")
                .setPositiveButton("Supprimer", (dialog, which) -> supprimerLivreDansRoom(livre))
                .setNegativeButton("Annuler", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}