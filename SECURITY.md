# Security Policy

## Supported Versions
| Version | Supported |
|---------|-----------|
| 1.0.x   | ✅ Active |

## Security Design
- Passwords hashed with BCrypt
- JWT tokens for API authentication
- Protected endpoints require valid tokens
- Immutable audit logs for compliance
- Per-service database isolation
