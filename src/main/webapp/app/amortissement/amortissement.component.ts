import { ConsoleLogger } from '@angular/compiler-cli';
import { Component } from '@angular/core';

@Component({
  selector: 'jhi-amortissement',
  templateUrl: './amortissement.component.html',
  styleUrls: ['./amortissement.component.scss'],
})
export class AmortissementComponent {
  montant: number = 0; // Assignez une valeur initiale à la propriété montant
  taux: number = 0; // Assi gnez une valeur initiale à la propriété taux
  duree: number = 0; // Assignez une valeur initiale à la propriété duree
  resultats: any[] = [];

  calculerAmortissement(): void {
    this.resultats = [];
    const nombreDeMois = 12;

    for (let i = 1; i <= this.duree; i++) {
      const annuite = (this.montant * this.taux * nombreDeMois) / (100 * 12);
      this.resultats.push({ annee: i, amortissement: annuite });
    }
    console.log(this.resultats);
  }
}
