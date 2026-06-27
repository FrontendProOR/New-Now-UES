import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AdminService, ManagerDto } from '../../services/admin.service';
import { LocationService } from '../../services/location.service';
import { AccountRequest, Location } from '../../models';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  pendingRequests: AccountRequest[] = [];
  locations: Location[] = [];
  locationForm: FormGroup;
  selectedImage: File | null = null;
  selectedPdf: File | null = null;
  activeTab = 0;

  managerEmail = '';
  assignLocationId: number | null = null;

  modalLocationId: number | null = null;
  modalLocationName = '';
  modalManagers: ManagerDto[] = [];
  showManagerModal = false;

  constructor(private adminService: AdminService,
              private locationService: LocationService,
              private fb: FormBuilder,
              private snackBar: MatSnackBar) {
    this.locationForm = this.fb.group({
      name: ['', Validators.required],
      address: ['', Validators.required],
      type: ['', Validators.required],
      description: ['']
    });
  }

  ngOnInit(): void {
    this.loadRequests();
    this.loadLocations();
  }

  loadRequests(): void {
    this.adminService.getPendingRequests().subscribe(data => this.pendingRequests = data);
  }

  loadLocations(): void {
    this.locationService.getAll().subscribe(data => this.locations = data);
  }

  acceptRequest(id: number): void {
    this.adminService.acceptRequest(id).subscribe({
      next: () => { this.snackBar.open('Request accepted', 'Close', { duration: 3000 }); this.loadRequests(); },
      error: (err) => this.snackBar.open(err.error?.error || 'Error', 'Close', { duration: 3000 })
    });
  }

  rejectRequest(id: number): void {
    const reason = prompt('Rejection reason (optional):');
    this.adminService.rejectRequest(id, reason || undefined).subscribe({
      next: () => { this.snackBar.open('Request rejected', 'Close', { duration: 3000 }); this.loadRequests(); },
      error: (err) => this.snackBar.open(err.error?.error || 'Error', 'Close', { duration: 3000 })
    });
  }

  onImageSelected(event: any): void { this.selectedImage = event.target.files[0]; }
  onPdfSelected(event: any): void { this.selectedPdf = event.target.files[0]; }

  createLocation(): void {
    if (this.locationForm.invalid || !this.selectedImage) return;
    const formData = new FormData();
    const val = this.locationForm.value;
    formData.append('name', val.name);
    formData.append('address', val.address);
    formData.append('type', val.type);
    if (val.description) formData.append('description', val.description);
    formData.append('image', this.selectedImage);

    this.adminService.createLocation(formData).subscribe({
      next: (loc) => {
        this.snackBar.open('Location created', 'Close', { duration: 3000 });
        if (this.selectedPdf) {
          const pdfData = new FormData();
          pdfData.append('pdf', this.selectedPdf);
          this.adminService.uploadPdf(loc.id, pdfData).subscribe();
        }
        this.locationForm.reset();
        this.selectedImage = null;
        this.selectedPdf = null;
        this.loadLocations();
      },
      error: (err) => this.snackBar.open(err.error?.error || 'Error creating location', 'Close', { duration: 3000 })
    });
  }

  deleteLocation(id: number): void {
    if (!confirm('Are you sure? This will delete all events, reviews, and managers for this location.')) return;
    this.adminService.deleteLocation(id).subscribe({
      next: () => { this.snackBar.open('Location deleted', 'Close', { duration: 3000 }); this.loadLocations(); },
      error: (err) => this.snackBar.open(err.error?.error || 'Error deleting location', 'Close', { duration: 3000 })
    });
  }

  assignManager(locationId: number): void {
    this.assignLocationId = locationId;
    this.managerEmail = '';
  }

  submitAssignManager(): void {
    if (!this.assignLocationId || !this.managerEmail.trim()) return;
    this.adminService.assignManager(this.assignLocationId, this.managerEmail.trim()).subscribe({
      next: () => {
        this.snackBar.open('Manager assigned', 'Close', { duration: 3000 });
        this.assignLocationId = null;
        this.managerEmail = '';
      },
      error: (err) => this.snackBar.open(err.error?.error || 'Error assigning manager', 'Close', { duration: 3000 })
    });
  }

  cancelAssign(): void {
    this.assignLocationId = null;
    this.managerEmail = '';
  }

  openManagerModal(loc: Location): void {
    this.modalLocationId = loc.id;
    this.modalLocationName = loc.name;
    this.showManagerModal = true;
    this.loadModalManagers();
  }

  loadModalManagers(): void {
    if (!this.modalLocationId) return;
    this.adminService.getManagers(this.modalLocationId).subscribe(data => this.modalManagers = data);
  }

  closeManagerModal(): void {
    this.showManagerModal = false;
    this.modalLocationId = null;
    this.modalManagers = [];
  }

  removeManagerFromModal(userId: number): void {
    if (!this.modalLocationId) return;
    this.adminService.removeManager(this.modalLocationId, userId).subscribe({
      next: () => {
        this.snackBar.open('Manager removed', 'Close', { duration: 3000 });
        this.loadModalManagers();
      },
      error: (err) => this.snackBar.open(err.error?.error || 'Error removing manager', 'Close', { duration: 3000 })
    });
  }
}
