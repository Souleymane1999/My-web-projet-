import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IImmobilisation } from '../immobilisation.model';

@Component({
  standalone: true,
  selector: 'jhi-immobilisation-detail',
  templateUrl: './immobilisation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ImmobilisationDetailComponent {
  montant: number = 50000; // Assignez une valeur initiale à la propriété montant
  taux: number = 20; // Assignez une valeur initiale à la propriété taux
  duree: number = 12; // Assignez une valeur initiale à la propriété duree
  resultats: any[] = [];

  @Input() immobilisation: IImmobilisation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }

  calculerAmortissement(taux: any): void {
    this.resultats = [];
    this.taux = taux.value;
    this.montant = Number(this.immobilisation?.valeur);
    this.duree = Number(this.immobilisation?.dureeAmortissement);

    const nombreDeMois = 12;

    for (let i = 1; i <= this.duree; i++) {
      const annuite = (this.montant * this.taux * nombreDeMois) / (100 * 12);
      this.resultats.push({ annee: i, amortissement: annuite });
    }
    console.log('tableau', this.immobilisation?.valeur);
  }
}
