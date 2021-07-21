package com.example.whitecollar.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.whitecollar.models.Picture;
import com.example.whitecollar.models.Shop;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pictures")
public class PictureController {
	private final PictureRepository pictureRepository;
	private final ShopRepository shopRepository;

	@Autowired
	public PictureController(PictureRepository pictureRepository, ShopRepository shopRepository) {
		this.pictureRepository = pictureRepository;
		this.shopRepository = shopRepository;
	}

	// Picture Create
	// pictureRepository.save(picture),create a new picture
	// Postman: Post-localhost:8080/api/v1/pictures
	@PostMapping
	public ResponseEntity<Picture> create(@RequestBody @Valid Picture picture) {
		Optional<Shop> optionalShop = shopRepository.findById(picture.getShop().getId());
		if (!optionalShop.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		picture.setShop(optionalShop.get());

		Picture savedPicture = pictureRepository.save(picture);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedPicture.getId())
				.toUri();

		return ResponseEntity.created(location).body(savedPicture);
	}

	// Picture Update By id
	// Update a child entity(picture) when updating an existing
	// parent(shop) parent
	// via pictureRepository.save(picture)
	// Postman: Put-localhost:8080/api/v1/pictures/{id}
	@PutMapping("/{id}")
	public ResponseEntity<Picture> update(@RequestBody @Valid Picture picture, @PathVariable Integer id) {
		Optional<Shop> optionalShop = shopRepository.findById(picture.getShop().getId());
		if (!optionalShop.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		Optional<Picture> optionalPicture = pictureRepository.findById(id);
		if (!optionalPicture.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		picture.setShop(optionalShop.get());
		picture.setId(optionalPicture.get().getId());
		pictureRepository.save(picture);

		return ResponseEntity.noContent().build();
	}
	// Delete a picture by ID
	// pictureRepository.delete(optionalPicture.get()); in the Delete API
	// Postman: Delete-localhost:8080/api/v1/pictures/{i}

	@DeleteMapping("/{id}")
	public ResponseEntity<Picture> delete(@PathVariable Integer id) {
		Optional<Picture> optionalPicture = pictureRepository.findById(id);
		if (!optionalPicture.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		pictureRepository.delete(optionalPicture.get());

		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<Page<Picture>> getAll(Pageable pageable) {
		return ResponseEntity.ok(pictureRepository.findAll(pageable));
	}

	// Retrieve a picture via pictureRepository.findById(id);
	// in the Get Shop API
	// Postman: Get-localhost:8080/api/v1/pictures/1
	@GetMapping("/{id}")
	public ResponseEntity<Picture> getById(@PathVariable Integer id) {
		Optional<Picture> optionalPicture = pictureRepository.findById(id);
		if (!optionalPicture.isPresent()) {
			return ResponseEntity.unprocessableEntity().build();
		}

		return ResponseEntity.ok(optionalPicture.get());
	}

	// Retrieve a list of child entities(picture) when retrieving a parent via
	// pictureRepository.findByShopId(shopId, pageable));
	// in the Get Shop API
	// Postman: Get-localhost:8080/api/v1/shops/1/
	@GetMapping("/shop/{shopId}")
	public ResponseEntity<Page<Picture>> getByShopId(@PathVariable Integer shopId, Pageable pageable) {
		return ResponseEntity.ok(pictureRepository.findByShopId(shopId, pageable));
	}
}