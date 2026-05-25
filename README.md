# 📚 BibliothequeApp

Application Android développée dans le cadre des TPs Android.

## 📋 Description

Application mobile Android pour la gestion d'une bibliothèque municipale. Développée en Java avec Android Studio.

## ✅ Labs réalisés

| Lab | Description | Statut |
| :--- | :--- | :--- |
| **Lab 1** | Installation Android Studio + JetBrains Toolbox | ✅ |
| **Lab 2** | Découverte de l'environnement Android Studio | ✅ |
| **Lab 3** | Écran d'accueil avec TextView, ImageView et Button | ✅ |
| **Lab 4** | Liste de livres avec RecyclerView et CardView | ✅ |
| **Lab 5** | Navigation entre écrans et fiche détail | ✅ |
| **Lab 6** | Ajout et modification de livres via un formulaire | ✅ |
| **Lab 7** | Persistance locale des données avec Room | ✅ |

## 📱 Fonctionnalités

* Écran d'accueil avec message de bienvenue.
* Liste dynamique de livres sous forme de cartes (`RecyclerView`).
* Formulaire d'ajout et d'édition de livres (`AddEditActivity`).
* Sauvegarde permanente des livres dans une base de données locale (`Room`).
* Badge vert **Disponible** pour les livres en stock.
* Badge rouge **Indisponible** pour les livres empruntés.
* Fiche détail complète pour chaque livre.
* Navigation fluide avec bouton de retour.
* Design moderne et épuré avec `CardView`.

## 🛠️ Technologies utilisées

* **Langage :** Java
* **IDE :** Android Studio
* **Base de données :** Room Persistence Library
* **SDK minimum :** API 24 (Android 7.0)
* **Composants :** RecyclerView, CardView, ConstraintLayout, Intent, Dao, AppDatabase