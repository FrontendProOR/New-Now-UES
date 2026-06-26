import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SearchService } from '../../services/search.service';
import { LocationSearchResult } from '../../models';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent {
  searchForm: FormGroup;
  results: LocationSearchResult[] = [];
  searched = false;
  sortDirection = 'asc';

  constructor(private searchService: SearchService, private fb: FormBuilder) {
    this.searchForm = this.fb.group({
      name: [''],
      description: [''],
      fileDescription: [''],
      reviewCountFrom: [null],
      reviewCountTo: [null],
      performanceFrom: [null],
      performanceTo: [null],
      soundFrom: [null],
      soundTo: [null],
      lightingFrom: [null],
      lightingTo: [null],
      spaceFrom: [null],
      spaceTo: [null],
      experienceFrom: [null],
      experienceTo: [null],
      operator: ['OR'],
      mlt: ['']
    });
  }

  onSearch(): void {
    const filters = {
      ...this.searchForm.value,
      sortDirection: this.sortDirection
    };
    this.searchService.searchLocations(filters).subscribe(data => {
      this.results = data;
      this.searched = true;
    });
  }

  onClear(): void {
    this.searchForm.reset({ operator: 'OR' });
    this.results = [];
    this.searched = false;
  }

  toggleSort(): void {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    if (this.searched) this.onSearch();
  }

  downloadPdf(locationId: number): void {
    this.searchService.downloadPdf(locationId).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `location-${locationId}.pdf`;
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
}
