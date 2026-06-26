import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { LocationService } from '../../services/location.service';
import { Location } from '../../models';

@Component({
  selector: 'app-location-list',
  templateUrl: './location-list.component.html',
  styleUrls: ['./location-list.component.scss']
})
export class LocationListComponent implements OnInit {
  locations: Location[] = [];
  searchForm: FormGroup;

  constructor(private locationService: LocationService, private fb: FormBuilder) {
    this.searchForm = this.fb.group({
      name: [''],
      address: [''],
      type: ['']
    });
  }

  ngOnInit(): void {
    this.loadLocations();
  }

  loadLocations(): void {
    this.locationService.getAll().subscribe(data => this.locations = data);
  }

  onSearch(): void {
    const { name, address, type } = this.searchForm.value;
    this.locationService.search(name || undefined, address || undefined, type || undefined)
      .subscribe(data => this.locations = data);
  }

  onClear(): void {
    this.searchForm.reset();
    this.loadLocations();
  }
}
