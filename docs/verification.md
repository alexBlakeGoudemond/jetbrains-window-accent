# Compatibility Verification

Every release of Window Accent is automatically validated using the JetBrains Plugin Verifier.

## Verification Pipeline

Once published to the JetBrains Marketplace, the following happens

```text
Plugin Verifier
   ↓
Publish Reports
   ↓
(On approval) available on JetBrains Marketplace
```

## Why this matters

The Plugin Verifier checks for:

- Binary compatibility
- Deprecated APIs
- Removed APIs
- Experimental API usage
- Compatibility across multiple IDE versions

Publishing the reports allows users and contributors to understand exactly which IDE versions have been tested.